package com.openmpy.wiki.global.data;

import com.openmpy.wiki.document.domain.constants.DocumentCategory;
import com.openmpy.wiki.document.domain.entity.Document;
import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import com.openmpy.wiki.global.snowflake.Snowflake;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    static final int EXECUTE_COUNT = 500;

    @Test
    void initialize() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.submit(() -> {
                insertDocument();
                latch.countDown();
                System.out.println("latch.getCount() = " + latch.getCount());
            });
        }

        latch.await();
        executorService.shutdown();
    }

    @Test
    void initializeWithHistory() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.submit(() -> {
                insertDocumentWithHistory();
                latch.countDown();
                System.out.println("latch.getCount() = " + latch.getCount());
            });
        }

        latch.await();
        executorService.shutdown();
    }

    void insertDocument() {
        transactionTemplate.executeWithoutResult(status -> {
            for (int i = 0; i < BULK_INSERT_SIZE; i++) {
                final DocumentCategory category = i % 2 == 0 ? DocumentCategory.RUNNER : DocumentCategory.GUILD;
                final Document document = Document.create(snowflake.nextId(), generate(), category);
                final DocumentHistory documentHistory = DocumentHistory.create(
                        snowflake.nextId(), generate(), generate(), "127.0.0.1", 1, document
                );

                document.addHistory(documentHistory);
                entityManager.persist(document);
            }
        });
    }

    void insertDocumentWithHistory() {
        transactionTemplate.executeWithoutResult(status -> {
            final String sql = """
                        INSERT INTO document_history (id, content, author, client_ip, version, document_id, deleted, created_at)
                        VALUES (?, ?, ?, ?, ?, ?, false, NOW())
                    """;

            for (int i = 0; i < BULK_INSERT_SIZE; i++) {
                entityManager.createNativeQuery(sql)
                        .setParameter(1, snowflake.nextId())
                        .setParameter(2, generate())
                        .setParameter(3, generate())
                        .setParameter(4, "127.0.0.1")
                        .setParameter(5, generateVersion(i))
                        .setParameter(6, "211690618052411392")
                        .executeUpdate();
            }
        });
    }

    String generate() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    Long generateVersion(final int i) {
        return Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())) * 1000 + i;
    }
}
