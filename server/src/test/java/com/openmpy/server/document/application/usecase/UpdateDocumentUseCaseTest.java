package com.openmpy.server.document.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.document.application.command.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.command.response.DocumentUpdateResponse;
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
class UpdateDocumentUseCaseTest {

    private static final String CLIENT_IP = "127.0.0.1";

    @Autowired
    private UpdateDocumentUseCase updateDocumentUseCase;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentImageRepository documentImageRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        em.createNativeQuery("DELETE FROM document").executeUpdate();
    }

    @Test
    void 문서가_정상적으로_수정된다_이미지_X() {
        final Document document = Document.create("제목", DocumentCategory.USER);
        documentRepository.save(document);

        // given
        final DocumentUpdateRequest request = new DocumentUpdateRequest(
                "홍길동_수정",
                "내용_수정",
                null
        );

        // when
        final DocumentUpdateResponse response = updateDocumentUseCase.execute(
                document.getId(),
                request,
                CLIENT_IP
        );

        // then
        final Document foundDocument = documentRepository.findById(document.getId()).orElseThrow();
        final DocumentHistory documentHistory = foundDocument.getHistories().getFirst();

        assertThat(response.documentId()).isNotNull();
        assertThat(documentHistory.getAuthor()).isEqualTo("홍길동_수정");
        assertThat(documentHistory.getContent()).isEqualTo("내용_수정");
        assertThat(documentHistory.getClientIp()).isEqualTo("127.0.0.1");
        assertThat(foundDocument.getImages()).isEmpty();
    }

    @Test
    void 문서가_정상적으로_수정된다_이미지_O() {
        final Document document = Document.create("제목", DocumentCategory.USER);
        documentRepository.save(document);

        final DocumentImage documentImage1 = DocumentImage.create("http://localhost:8080/image/1", CLIENT_IP);
        final DocumentImage documentImage2 = DocumentImage.create("http://localhost:8080/image/2", CLIENT_IP);
        final DocumentImage documentImage3 = DocumentImage.create("http://localhost:8080/image/3", CLIENT_IP);

        documentImageRepository.saveAll(List.of(documentImage1, documentImage2, documentImage3));

        // given
        final DocumentUpdateRequest request = new DocumentUpdateRequest(
                "홍길동_수정",
                "내용_수정",
                List.of(documentImage1.getId(), documentImage2.getId(), documentImage3.getId())
        );

        // when
        final DocumentUpdateResponse response = updateDocumentUseCase.execute(
                document.getId(),
                request,
                CLIENT_IP
        );

        // then
        final Document foundDocument = documentRepository.findById(document.getId()).orElseThrow();
        final DocumentHistory documentHistory = foundDocument.getHistories().getFirst();

        assertThat(response.documentId()).isNotNull();
        assertThat(documentHistory.getAuthor()).isEqualTo("홍길동_수정");
        assertThat(documentHistory.getContent()).isEqualTo("내용_수정");
        assertThat(documentHistory.getClientIp()).isEqualTo("127.0.0.1");
        assertThat(foundDocument.getImages()).hasSize(3);
    }

    @Test
    void 수정할_문서_번호를_찾을_수_없을_경우_예외가_발생한다() {
        // given
        final DocumentUpdateRequest request = new DocumentUpdateRequest(
                "홍길동_수정",
                "내용_수정",
                null
        );

        // when & then
        assertThatThrownBy(() -> updateDocumentUseCase.execute(999L, request, CLIENT_IP))
                .isInstanceOf(CustomException.class)
                .hasMessage("찾을 수 없는 문서 번호입니다.");
    }
}