package com.openmpy.server.global.dummy;

import static com.openmpy.server.document.domain.type.DocumentCategory.GUILD;
import static com.openmpy.server.document.domain.type.DocumentCategory.USER;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DummyDataInit {

    private static final int INITIAL_DOCUMENT_COUNT = 10000;
    private static final int INITIAL_DOCUMENT_HISTORY_COUNT = 5;
    private static final int BATCH_SIZE = 20;

    private final EntityManager em;
    private final PlatformTransactionManager txManager;

    @ConditionalOnProperty(name = "app.dummy.enabled", havingValue = "true")
    @Profile("local")
    @Bean
    CommandLineRunner init(final DocumentRepository documentRepository) {
        return args -> {
            final TransactionTemplate tx = new TransactionTemplate(txManager);

            tx.executeWithoutResult(status -> {
                em.createNativeQuery("TRUNCATE TABLE document RESTART IDENTITY CASCADE").executeUpdate();
                em.flush();
                em.clear();

                final List<Document> buffer = new ArrayList<>(BATCH_SIZE);

                for (int i = 0; i < INITIAL_DOCUMENT_COUNT; i++) {
                    final Document document = Document.create("제목" + i, i % 2 == 0 ? USER : GUILD);

                    for (int j = 0; j < INITIAL_DOCUMENT_HISTORY_COUNT; j++) {
                        final long imageId = j;
                        final String ip = "127.0.0." + (j % 255 + 1);
                        document.addHistory("작성자" + j, "내용" + j, imageId, ip);
                    }

                    buffer.add(document);

                    if (buffer.size() >= BATCH_SIZE) {
                        documentRepository.saveAll(buffer);
                        em.flush();
                        em.clear();
                        buffer.clear();
                    }
                }

                if (!buffer.isEmpty()) {
                    documentRepository.saveAll(buffer);
                    em.flush();
                    em.clear();
                    buffer.clear();
                }

                log.info("문서 더미 데이터 입력 완료. 문서 수 = {}", documentRepository.count());
            });
        };
    }
}
