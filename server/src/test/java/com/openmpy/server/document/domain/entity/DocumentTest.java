package com.openmpy.server.document.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DocumentTest {

    @DisplayName("문서 객체를 생성한다.")
    @Test
    void document_test_01() {
        // given & when
        final Document document = Document.create("제목", DocumentCategory.USER);

        // then
        assertThat(document.getTitle()).isEqualTo("제목");
        assertThat(document.getTitleChosung()).isEqualTo("ㅈㅁ");
        assertThat(document.getCategory()).isEqualTo(DocumentCategory.USER);
        assertThat(document.getLatestVersion()).isZero();
        assertThat(document.getIsDeleted()).isFalse();
    }

    @DisplayName("문서 객체에 문서 기록 객체를 추가한다.")
    @Test
    void document_test_02() {
        // given
        final Document document = Document.create("제목", DocumentCategory.USER);

        // when
        document.addHistory(
            "작성자",
            "내용",
            10L,
            "127.0.0.1"
        );

        // then
        final DocumentHistory firstHistory = document.getHistories().getFirst();

        assertThat(document.getHistories()).hasSize(1);
        assertThat(firstHistory.getDocument()).isEqualTo(document);
        assertThat(firstHistory.getAuthor()).isEqualTo("작성자");
        assertThat(firstHistory.getContent()).isEqualTo("내용");
        assertThat(firstHistory.getSize()).isEqualTo(10L);
        assertThat(firstHistory.getVersion()).isEqualTo(1);
        assertThat(firstHistory.getClientIp()).isEqualTo("127.0.0.1");
    }

    @DisplayName("문서를 삭제한다.")
    @Test
    void document_test_04() {
        // given
        final Document document = Document.create("제목", DocumentCategory.USER);

        // when
        document.delete();

        // then
        assertThat(document.getIsDeleted()).isTrue();
        assertThat(document.getDeletedAt()).isNotNull();
    }

    @DisplayName("삭제된 문서 객체에 문서 기록 객체를 추가하면 예외가 발생한다.")
    @Test
    void exception_document_test_01() {
        // given
        final Document document = Document.create("제목", DocumentCategory.USER);

        ReflectionTestUtils.setField(document, "isDeleted", true);

        // when & then
        assertThatThrownBy(() -> document.addHistory("작성자", "내용", 10L, "127.0.0.1"))
            .isInstanceOf(CustomException.class)
            .hasMessage("삭제된 문서에 추가할 수 없습니다.");
    }

    @DisplayName("이미 삭제된 문서를 삭제할 경우 예외가 발생한다.")
    @Test
    void exception_document_test_04() {
        // given
        final Document document = Document.create("제목", DocumentCategory.USER);

        ReflectionTestUtils.setField(document, "isDeleted", true);

        // when & then
        assertThatThrownBy(document::delete)
            .isInstanceOf(CustomException.class)
            .hasMessage("이미 삭제된 문서입니다.");
    }
}