package com.openmpy.server.document.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.openmpy.server.document.application.port.ImageStorage;
import com.openmpy.server.document.domain.model.DocumentImage;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class DeleteAllDocumentImagesUseCaseTest {

    @Autowired
    private DeleteAllDocumentImagesUseCase deleteAllDocumentImagesUseCase;

    @Autowired
    private DocumentImageRepository documentImageRepository;

    @MockitoBean
    private ImageStorage imageStorage;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        em.createNativeQuery("DELETE FROM document_image").executeUpdate();
    }

    @Test
    void 만료된_TEMP_이미지를_모두_삭제한다() {
        // given
        final DocumentImage documentImage1 = DocumentImage.create(
                "https://mock.s3/1.png",
                "127.0.0.1"
        );
        final DocumentImage documentImage2 = DocumentImage.create(
                "https://mock.s3/2.png",
                "127.0.0.1"
        );

        ReflectionTestUtils.setField(documentImage1, "expiredAt", LocalDateTime.now().minusDays(1));
        ReflectionTestUtils.setField(documentImage2, "expiredAt", LocalDateTime.now().minusDays(1));
        documentImageRepository.saveAll(List.of(documentImage1, documentImage2));

        // stub
        doNothing().when(imageStorage).delete(anyString());

        // when
        final long deletedImageCount = deleteAllDocumentImagesUseCase.execute();

        // then
        assertThat(deletedImageCount).isEqualTo(2);
        assertThat(documentImageRepository.count()).isZero();

        verify(imageStorage, times(2)).delete(anyString());
    }

    @Test
    void 이미지_삭제_중_하나라도_실패하면_예외가_발생한다() {
        // given
        final DocumentImage documentImage = DocumentImage.create(
                "https://mock.s3/1.png",
                "127.0.0.1"
        );

        ReflectionTestUtils.setField(documentImage, "expiredAt", LocalDateTime.now().minusDays(1));
        documentImageRepository.save(documentImage);

        // when
        doThrow(new RuntimeException("S3 error")).when(imageStorage).delete(anyString());

        // then
        assertThatThrownBy(() -> deleteAllDocumentImagesUseCase.execute())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미지 삭제 실패");

        assertThat(documentImageRepository.count()).isEqualTo(1);
    }
}