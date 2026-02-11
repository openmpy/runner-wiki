package com.openmpy.server.document.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DocumentHistoryTest {

    @DisplayName("문서 기록 객체를 생성한다.")
    @Test
    void document_history_test_01() {
        // given & when
        final DocumentHistory history = DocumentHistory.create(
            "작성자",
            "내용",
            1,
            10L,
            "127.0.0.1"
        );

        // then
        assertThat(history.getAuthor()).isEqualTo("작성자");
        assertThat(history.getContent()).isEqualTo("내용");
        assertThat(history.getVersion()).isEqualTo(1);
        assertThat(history.getSize()).isEqualTo(10L);
        assertThat(history.getClientIp()).isEqualTo("127.0.0.1");
        assertThat(history.getIsDeleted()).isFalse();
    }

    @DisplayName("문서 기록 객체를 문서 객체에 할당한다.")
    @Test
    void document_history_test_02() {
        // given
        final Document document = new Document();
        final DocumentHistory history = new DocumentHistory();

        // when
        history.assignTo(document);

        // then
        assertThat(history.getDocument()).isEqualTo(document);
    }

    @DisplayName("문서 기록 객체를 삭제한다.")
    @Test
    void document_history_test_03() {
        // given
        final DocumentHistory history = new DocumentHistory();

        // when
        history.delete();

        // then
        assertThat(history.getIsDeleted()).isTrue();
        assertThat(history.getDeletedAt()).isNotNull();
    }

    @DisplayName("문서 객체에 할당할 때 문서 객체 값이 null이면 예외가 발생한다.")
    @Test
    void exception_document_history_test_01() {
        // given
        final DocumentHistory history = new DocumentHistory();

        // when & then
        assertThatThrownBy(() -> history.assignTo(null))
            .isInstanceOf(CustomException.class)
            .hasMessage("문서가 null 값입니다.");
    }
}