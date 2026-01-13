package com.openmpy.server.document.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openmpy.server.document.application.command.dto.response.DocumentImageUploadResponses;
import com.openmpy.server.document.application.command.usecase.UploadDocumentImagesUseCase;
import com.openmpy.server.document.application.image.port.ImageStorage;
import com.openmpy.server.document.application.image.port.UploadedImage;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class UploadDocumentImagesUseCaseTest {

    private static final String CLIENT_IP = "127.0.0.1";

    @Autowired
    private UploadDocumentImagesUseCase uploadDocumentImagesUseCase;

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
    void 문서에_이미지_목록을_업로드한다() {
        // given
        final MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "images",
                "test.jpg",
                "image/jpeg",
                new byte[0]
        );
        final UploadedImage uploadedImage = new UploadedImage("https://mock.s3/test.jpg");

        // stub
        when(imageStorage.upload(any())).thenReturn(uploadedImage);

        // when
        final DocumentImageUploadResponses responses = uploadDocumentImagesUseCase.execute(
                List.of(mockMultipartFile),
                CLIENT_IP
        );

        // then
        assertThat(responses.images()).hasSize(1);
        assertThat(responses.images().getFirst().url()).isEqualTo("https://mock.s3/test.jpg");
        assertThat(documentImageRepository.count()).isEqualTo(1);

        verify(imageStorage, times(1)).upload(any());
    }
}