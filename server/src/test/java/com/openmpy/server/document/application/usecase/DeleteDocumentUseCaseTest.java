package com.openmpy.server.document.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.global.exception.CustomException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class DeleteDocumentUseCaseTest {

    @Autowired
    private DeleteDocumentUseCase deleteDocumentUseCase;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        em.createNativeQuery("DELETE FROM document").executeUpdate();
    }

    @Test
    void 문서가_정상적으로_삭제된다() {
        // given
        final Document document = Document.create("제목", DocumentCategory.USER);

        documentRepository.save(document);

        // when
        deleteDocumentUseCase.execute(document.getId());

        // then
        final long count = documentRepository.count();

        assertThat(count).isZero();
    }

    @Test
    void 삭제할_문서_번호를_찾을_수_없을_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> deleteDocumentUseCase.execute(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage("찾을 수 없는 문서 번호입니다.");
    }
}