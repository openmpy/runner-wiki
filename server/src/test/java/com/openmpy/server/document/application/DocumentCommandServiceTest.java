package com.openmpy.server.document.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.entity.DocumentHistory;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.document.dto.request.DocumentCreateRequest;
import com.openmpy.server.document.dto.request.DocumentUpdateRequest;
import com.openmpy.server.document.dto.response.DocumentCreateResponse;
import com.openmpy.server.document.dto.response.DocumentUpdateResponse;
import com.openmpy.server.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
class DocumentCommandServiceTest {

    @Autowired
    private DocumentCommandService documentCommandService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @BeforeEach
    void setUp() {
        documentRepository.deleteAll();
    }

    @DisplayName("이미지가 없는 문서를 작성한다.")
    @Test
    void document_command_service_test_01() {
        // given
        final DocumentCreateRequest request = new DocumentCreateRequest(
            "제목",
            DocumentCategory.USER,
            "작성자",
            "내용",
            null,
            "success-token"
        );

        // when
        final DocumentCreateResponse response = documentCommandService.save(request, "127.0.0.1");

        // then
        final Document foundDocument = documentRepository.findAll().getFirst();
        final DocumentHistory foundDocumentHistory = foundDocument.getHistories().getFirst();

        assertThat(response.documentId()).isNotNull();
        assertThat(foundDocument.getId()).isNotNull();
        assertThat(foundDocument.getTitle()).isEqualTo("제목");
        assertThat(foundDocument.getCategory()).isEqualTo(DocumentCategory.USER);
        assertThat(foundDocument.getLatestVersion()).isEqualTo(1);

        assertThat(foundDocumentHistory.getAuthor()).isEqualTo("작성자");
        assertThat(foundDocumentHistory.getContent()).isEqualTo("내용");
        assertThat(foundDocumentHistory.getVersion()).isEqualTo(1);
        assertThat(foundDocumentHistory.getSize()).isEqualTo(6);
        assertThat(foundDocumentHistory.getClientIp()).isEqualTo("127.0.0.1");
    }

    @DisplayName("작성된 문서에 이미지가 포함 되지 않은 내용으로 수정한다.")
    @Test
    void document_command_service_test_03() {
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
        final DocumentUpdateRequest request = new DocumentUpdateRequest(
            "작성자2",
            "내용2",
            null,
            "success-token"
        );
        final DocumentUpdateResponse response = documentCommandService.update(
            document.getId(),
            request,
            "127.0.0.1"
        );

        // then
        final Document foundDocument = documentRepository.findAll().getFirst();
        final DocumentHistory foundDocumentHistory = foundDocument.getHistories().getLast();

        assertThat(response.documentId()).isNotNull();

        assertThat(foundDocumentHistory.getAuthor()).isEqualTo("작성자2");
        assertThat(foundDocumentHistory.getContent()).isEqualTo("내용2");
        assertThat(foundDocumentHistory.getVersion()).isEqualTo(2);
        assertThat(foundDocumentHistory.getSize()).isEqualTo(7);
        assertThat(foundDocumentHistory.getClientIp()).isEqualTo("127.0.0.1");
    }

    @DisplayName("문서를 삭제한다.")
    @Test
    void document_command_service_test_05() {
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
        documentCommandService.delete(document.getId());

        // then
        assertThat(documentRepository.count()).isZero();
        assertThat(documentHistoryRepository.count()).isZero();
    }

    @DisplayName("동일한 카테고리에 이미 작성된 문서일 경우 예외가 발생한다.")
    @Test
    void exception_document_command_service_test_01() {
        // given
        final Document document = Document.create(
            "제목",
            DocumentCategory.USER
        );
        documentRepository.save(document);

        // when & then
        final DocumentCreateRequest request = new DocumentCreateRequest(
            "제목",
            DocumentCategory.USER,
            "작성자",
            "내용",
            null,
            "success-token"
        );

        assertThatThrownBy(() -> documentCommandService.save(request, "127.0.0.1"))
            .isInstanceOf(CustomException.class)
            .hasMessage("이미 작성된 문서입니다.");
    }
}