package com.openmpy.server.document.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.model.DocumentHistory;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class DeleteDocumentHistoryUseCaseTest {

    @Autowired
    private DeleteDocumentHistoryUseCase deleteDocumentHistoryUseCase;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @BeforeEach
    void setUp() {
        documentRepository.deleteAll();
    }

    @Transactional
    @Test
    void 문서_기록이_정상적으로_삭제된다() {
        // given
        final Document document = Document.create("제목", DocumentCategory.USER);

        document.addHistory("홍길동", "내용", 1L, "127.0.0.1");
        documentRepository.save(document);

        // when
        final DocumentHistory documentHistory = document.getHistories().getFirst();

        deleteDocumentHistoryUseCase.execute(documentHistory.getId());

        // then
        final long count = documentHistoryRepository.count();

        assertThat(count).isZero();
    }

    @Test
    void 삭제할_문서_기록을_찾을_수_없을_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> deleteDocumentHistoryUseCase.execute(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage("찾을 수 없는 문서 기록 번호입니다.");
    }
}