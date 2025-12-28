package com.openmpy.server.global.dummy;

import static com.openmpy.server.document.domain.type.DocumentCategory.GUILD;
import static com.openmpy.server.document.domain.type.DocumentCategory.USER;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
public class DummyDataInit {

    private static final int INITIAL_DOCUMENT_COUNT = 1000;

    @Profile("local")
    @Bean
    CommandLineRunner init(final DocumentRepository documentRepository) {
        return args -> {
            for (int i = 0; i < INITIAL_DOCUMENT_COUNT; i++) {
                final Document document = Document.create("제목" + i, i % 2 == 0 ? USER : GUILD);

                document.addHistory("작성자" + i, "내용" + i, (long) i, "127.0.0." + i);
                documentRepository.save(document);
            }

            log.info("문서 더미 데이터가 입력되었습니다. {}개", documentRepository.count());
        };
    }
}
