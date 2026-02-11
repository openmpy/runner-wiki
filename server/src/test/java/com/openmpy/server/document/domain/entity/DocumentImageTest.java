package com.openmpy.server.document.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.document.domain.type.DocumentImageStatus;
import com.openmpy.server.global.exception.CustomException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DocumentImageTest {

    public final DocumentImage DOCUMENT_IMAGE = DocumentImage.create(
        "http://test.com/image",
        "127.0.0.1"
    );

    @DisplayName("문서 이미지 객체를 생성한다.")
    @Test
    void document_image_test_01() {
        // given & when
        final DocumentImage image = DOCUMENT_IMAGE;

        // then
        assertThat(image.getUrl()).isEqualTo("http://test.com/image");
        assertThat(image.getStatus()).isEqualTo(DocumentImageStatus.TEMP);
        assertThat(image.getClientIp()).isEqualTo("127.0.0.1");
        assertThat(image.getExpiredAt()).isNotNull();
    }

    @DisplayName("문서 이미지 객체를 문서 객체에 사용한다.")
    @Test
    void document_image_test_02() {
        // given
        final Document document = new Document();
        final DocumentImage image = DOCUMENT_IMAGE;

        // when
        image.markAsUsed(document);

        // then
        assertThat(image.getDocument()).isEqualTo(document);
        assertThat(image.getStatus()).isEqualTo(DocumentImageStatus.USED);
        assertThat(image.getExpiredAt()).isNull();
    }

    @DisplayName("이미지를 삭제한다.")
    @Test
    void document_image_test_03() {
        // given
        final DocumentImage image = new DocumentImage();

        // when
        image.delete();

        // then
        assertThat(image.getDeletedAt()).isNotNull();
    }

    @DisplayName("이미지를 사용할 때 문서가 null 값이면 예외가 발생한다.")
    @Test
    void exception_document_image_test_01() {
        // given
        final DocumentImage image = new DocumentImage();

        // when & then
        assertThatThrownBy(() -> image.markAsUsed(null))
            .isInstanceOf(CustomException.class)
            .hasMessage("문서가 null 값입니다.");
    }

    @DisplayName("이미지를 사용할 때 TEMP 상태가 아니면 예외가 발생한다.")
    @Test
    void exception_document_image_test_02() {
        // given
        final Document document = new Document();
        final DocumentImage image = new DocumentImage();

        ReflectionTestUtils.setField(image, "status", DocumentImageStatus.USED);

        // when & then
        assertThatThrownBy(() -> image.markAsUsed(document))
            .isInstanceOf(CustomException.class)
            .hasMessage("TEMP 이미지만 사용할 수 있습니다.");
    }

    @DisplayName("이미지를 사용할 때 만료된 이미지이면 예외가 발생한다.")
    @Test
    void exception_document_image_test_03() {
        // given
        final Document document = new Document();
        final DocumentImage image = DOCUMENT_IMAGE;

        ReflectionTestUtils.setField(image, "expiredAt", LocalDateTime.now().minusHours(24));

        // when & then
        assertThatThrownBy(() -> image.markAsUsed(document))
            .isInstanceOf(CustomException.class)
            .hasMessage("만료된 이미지입니다.");
    }

    @DisplayName("이미지를 사용할 때 삭제된 이미지이면 예외가 발생한다.")
    @Test
    void exception_document_image_test_04() {
        // given
        final Document document = new Document();
        final DocumentImage image = DOCUMENT_IMAGE;

        ReflectionTestUtils.setField(image, "deletedAt", LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> image.markAsUsed(document))
            .isInstanceOf(CustomException.class)
            .hasMessage("이미 삭제된 이미지입니다.");
    }
}