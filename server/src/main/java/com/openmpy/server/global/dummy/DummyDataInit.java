package com.openmpy.server.global.dummy;

import static com.openmpy.server.document.domain.type.DocumentCategory.GUILD;
import static com.openmpy.server.document.domain.type.DocumentCategory.USER;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
public class DummyDataInit {

    private static final int INITIAL_DOCUMENT_COUNT = 100;
    private static final int INITIAL_DOCUMENT_HISTORY_COUNT = 50;

    @Profile("local")
    @Bean
    CommandLineRunner init(final DocumentRepository documentRepository) {
        documentRepository.deleteAll();

        return args -> {
            final List<Document> documents = new ArrayList<>();

            for (int i = 0; i < INITIAL_DOCUMENT_COUNT; i++) {
                final Document document = Document.create("제목" + i, i % 2 == 0 ? USER : GUILD);

                for (int j = 0; j < INITIAL_DOCUMENT_HISTORY_COUNT; j++) {
                    document.addHistory("작성자" + j, "내용" + j, (long) j, "127.0.0." + j);
                }
                documents.add(document);
            }

            documentRepository.saveAll(documents);
            log.info("문서 더미 데이터가 입력되었습니다. {}개", documentRepository.count());
        };
    }
}
