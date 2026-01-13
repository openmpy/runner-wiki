package com.openmpy.server.document.application.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.server.document.application.command.dto.request.DocumentCreateRequest;
import com.openmpy.server.document.application.command.service.DocumentCommandService;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import jakarta.persistence.EntityManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class CreateDocumentConcurrencyTest {

    @Autowired
    private DocumentCommandService commandService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        em.createNativeQuery("DELETE FROM document").executeUpdate();
    }

    @Test
    void 같은_슬러그로_동시에_요청하면_1개만_생겨야_한다() throws Exception {
        // given
        final int threadCount = 100;
        final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        final CountDownLatch ready = new CountDownLatch(threadCount);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(threadCount);

        final AtomicInteger success = new AtomicInteger(0);
        final AtomicInteger fail = new AtomicInteger(0);

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                ready.countDown();

                try {
                    start.await();

                    final DocumentCreateRequest request = new DocumentCreateRequest(
                            "동시성제목",
                            DocumentCategory.USER,
                            "홍길동",
                            "내용",
                            null
                    );

                    commandService.create(request, "127.0.0.1");
                    success.incrementAndGet();
                } catch (final Exception e) {
                    e.printStackTrace();
                    fail.incrementAndGet();
                } finally {
                    done.countDown();
                }
            });
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
