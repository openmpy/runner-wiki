package com.openmpy.server.document.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
class DocumentHistoryCommandServiceTest {

    @Autowired
    private DocumentHistoryCommandService documentHistoryCommandService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @BeforeEach
    void setUp() {
        documentHistoryRepository.deleteAll();
        documentRepository.deleteAll();
    }

    @DisplayName("문서 기록을 삭제한다.")
    @Test
    void document_history_command_service_test_01() {
        // given
        final Document document = Document.create(
            "제목",
            DocumentCategory.USER
        );
        documentRepository.save(document);

        document.addHistory(
            "작성자",
            "내용",
            10L,
            "127.0.0.1"
        );

        // when
        final Long historyId = documentHistoryRepository.findAll().getFirst().getId();

        documentHistoryCommandService.delete(historyId);

        // then
        assertThat(documentHistoryRepository.count()).isZero();
    }
}