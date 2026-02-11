package com.openmpy.server.document.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.entity.DocumentHistory;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.document.dto.request.DocumentCreateRequest;
import com.openmpy.server.document.dto.request.DocumentUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class DocumentServiceConcurrencyTest {

    @Autowired
    private DocumentCommandService documentCommandService;

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    void setUp() {
        documentHistoryRepository.deleteAll();
        documentRepository.deleteAll();
    }

    @DisplayName("같은 문서를 동시에 생성하면 하나만 저장된다.")
    @Test
    void document_service_concurrency_test_01() throws Exception {
        // given
        final int threadCount = 20;
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        final CountDownLatch ready = new CountDownLatch(threadCount);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(threadCount);

        final AtomicInteger success = new AtomicInteger(0);
        final AtomicInteger fail = new AtomicInteger(0);

        final ConcurrentLinkedQueue<Throwable> errors = new ConcurrentLinkedQueue<>();
        final List<Future<?>> futures = new ArrayList<>();

        final DocumentCreateRequest request = new DocumentCreateRequest(
            "제목",
            DocumentCategory.USER,
            "작성자",
            "내용",
            null,
            "success-token"
        );
        final String clientIp = "127.0.0.1";

        // when
        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                ready.countDown();

                try {
                    start.await();

                    documentCommandService.save(request, clientIp);
                    success.incrementAndGet();
                } catch (final Throwable t) {
                    fail.incrementAndGet();
                    errors.add(t);
                } finally {
                    done.countDown();
                }
            }));
        }

        ready.await();
        start.countDown();
        done.await();
        executor.shutdown();

        // then
        assertThat(success.get()).isEqualTo(1);
        assertThat(fail.get()).isEqualTo(threadCount - 1);

        assertThat(documentRepository.count()).isEqualTo(1);
    }

    @DisplayName("같은 문서를 동시에 수정하면 기록 버전이 중복/누락 없이 증가한다.")
    @Test
    void document_service_concurrency_test_02() throws Exception {
        // given
        final int threadCount = 20;
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        final CountDownLatch ready = new CountDownLatch(threadCount);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(threadCount);

        final AtomicInteger success = new AtomicInteger(0);
        final AtomicInteger fail = new AtomicInteger(0);

        final ConcurrentLinkedQueue<Throwable> errors = new ConcurrentLinkedQueue<>();
        final List<Future<?>> futures = new ArrayList<>();

        final Document document = Document.create("제목", DocumentCategory.USER);
        documentRepository.saveAndFlush(document);

        // when
        for (int i = 0; i < threadCount; i++) {
            final int idx = i;

            futures.add(executor.submit(() -> {
                ready.countDown();

                try {
                    start.await();

                    final DocumentUpdateRequest request = new DocumentUpdateRequest(
                        "작성자-" + idx,
                        "내용-" + idx,
                        null,
                        "success-token"
                    );
                    final String clientIp = "127.0.0.1";

                    documentCommandService.update(document.getId(), request, clientIp);
                    success.incrementAndGet();
                } catch (final Throwable t) {
                    fail.incrementAndGet();
                    errors.add(t);
                } finally {
                    done.countDown();
                }
            }));
        }

        ready.await();
        start.countDown();
        done.await();
        executor.shutdown();

        // then
        assertThat(success.get()).isEqualTo(threadCount);
        assertThat(fail.get()).isZero();

        final List<DocumentHistory> foundHistories = documentHistoryRepository.findAllByDocument_Id(
            document.getId()
        );
        assertThat(foundHistories).hasSize(threadCount);

        final List<Integer> versions = documentHistoryRepository.findAllVersionsByDocumentId(
            document.getId()
        );
        versions.sort(Integer::compareTo);

        assertThat(versions.getFirst()).isEqualTo(1);
        assertThat(versions.getLast()).isEqualTo(threadCount);
        assertThat(versions).doesNotHaveDuplicates();

        foundHistories.forEach(history -> System.out.println("history = " + history.getVersion()));
    }
}
