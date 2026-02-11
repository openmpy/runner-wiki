package com.openmpy.server.document.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.document.dto.request.DocumentCreateRequest;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
public class DocumentServiceConcurrencyTest {

    @Autowired
    private DocumentCommandService documentCommandService;

    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    void setUp() {
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
}
