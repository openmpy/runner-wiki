package com.openmpy.server.document.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.document.application.command.dto.request.DocumentCreateRequest;
import com.openmpy.server.document.application.command.dto.response.DocumentCreateResponse;
import com.openmpy.server.document.application.command.usecase.CreateDocumentUseCase;
import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.model.DocumentHistory;
import com.openmpy.server.document.domain.model.DocumentImage;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.global.exception.CustomException;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class CreateDocumentUseCaseTest {

    private static final String CLIENT_IP = "127.0.0.1";

    @Autowired
    private CreateDocumentUseCase createDocumentUseCase;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentImageRepository documentImageRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        em.createNativeQuery("DELETE FROM document_history").executeUpdate();
        em.createNativeQuery("DELETE FROM document").executeUpdate();
    }

    @Test
    void 문서가_정상적으로_작성된다_이미지_X() {
        // given
        final DocumentCreateRequest request = new DocumentCreateRequest(
                "제목",
                DocumentCategory.USER,
                "홍길동",
                "내용",
                null
        );

        // when
        final DocumentCreateResponse response = createDocumentUseCase.execute(request, CLIENT_IP);

        // then
        final Document document = documentRepository.findAll().getFirst();
        final DocumentHistory documentHistory = document.getHistories().getFirst();

        assertThat(response.documentId()).isNotNull();
        assertThat(document.getTitle()).isEqualTo("제목");
        assertThat(documentHistory.getAuthor()).isEqualTo("홍길동");
        assertThat(documentHistory.getContent()).isEqualTo("내용");
        assertThat(documentHistory.getClientIp()).isEqualTo("127.0.0.1");
        assertThat(document.getImages()).isEmpty();
    }

    @Test
    void 문서가_정상적으로_작성된다_이미지_O() {
        final DocumentImage documentImage1 = DocumentImage.create("http://localhost:8080/image/1", CLIENT_IP);
        final DocumentImage documentImage2 = DocumentImage.create("http://localhost:8080/image/2", CLIENT_IP);
        final DocumentImage documentImage3 = DocumentImage.create("http://localhost:8080/image/3", CLIENT_IP);

        documentImageRepository.saveAll(List.of(documentImage1, documentImage2, documentImage3));

        // given
        final DocumentCreateRequest request = new DocumentCreateRequest(
                "제목",
                DocumentCategory.USER,
                "홍길동",
                "내용",
                List.of(documentImage1.getId(), documentImage2.getId(), documentImage3.getId())
        );

        // when
        final DocumentCreateResponse response = createDocumentUseCase.execute(request, CLIENT_IP);

        // then
        final Document document = documentRepository.findAll().getFirst();
        final DocumentHistory documentHistory = document.getHistories().getFirst();

        assertThat(response.documentId()).isNotNull();
        assertThat(document.getTitle()).isEqualTo("제목");
        assertThat(documentHistory.getAuthor()).isEqualTo("홍길동");
        assertThat(documentHistory.getContent()).isEqualTo("내용");
        assertThat(documentHistory.getClientIp()).isEqualTo("127.0.0.1");
        assertThat(document.getImages()).hasSize(3);
    }

    @Test
    void 이미_작성된_문서일_경우_예외가_발생한다() {
        final Document document = Document.create("제목", DocumentCategory.USER);

        documentRepository.save(document);

        // given
        final DocumentCreateRequest request = new DocumentCreateRequest(
                "제목",
                DocumentCategory.USER,
                "홍길동",
                "내용",
                null
        );

        // when & then
        assertThatThrownBy(() -> createDocumentUseCase.execute(request, CLIENT_IP))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 작성된 문서입니다.");
    }
}