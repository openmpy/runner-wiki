package com.openmpy.wiki.global.data;

import com.openmpy.wiki.document.domain.constants.DocumentCategory;
import com.openmpy.wiki.document.domain.entity.Document;
import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import com.openmpy.wiki.global.snowflake.Snowflake;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class DataInitializer {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TransactionTemplate transactionTemplate;

    Snowflake snowflake = new Snowflake();
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    static final int BULK_INSERT_SIZE = 2000;
    static final int EXECUTE_COUNT = 5000;

    @Test
    void initialize() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.submit(() -> {
                insert();
                latch.countDown();
                System.out.println("latch.getCount() = " + latch.getCount());
            });
        }

        latch.await();
        executorService.shutdown();
    }

    void insert() {
        transactionTemplate.executeWithoutResult(status -> {
            for (int i = 0; i < BULK_INSERT_SIZE; i++) {
                final DocumentCategory category = i % 2 == 0 ? DocumentCategory.RUNNER : DocumentCategory.GUILD;
                final Document document = Document.create(snowflake.nextId(), generate(), category);
                final DocumentHistory documentHistory = DocumentHistory.create(
                        snowflake.nextId(), generate(), generate(), "127.0.0.1", document
                );

                document.addHistory(documentHistory);
                entityManager.persist(document);
            }
        });
    }

    String generate() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}
