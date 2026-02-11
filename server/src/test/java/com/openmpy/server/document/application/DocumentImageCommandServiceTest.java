package com.openmpy.server.document.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.entity.DocumentImage;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.document.domain.type.DocumentImageStatus;
import com.openmpy.server.document.dto.response.DocumentImageUploadResponses;
import com.openmpy.server.global.exception.CustomException;
import com.openmpy.server.image.application.port.ImageStoragePort;
import com.openmpy.server.image.dto.UploadedImage;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
class DocumentImageCommandServiceTest {

    @Autowired
    private DocumentImageCommandService documentImageCommandService;

    @Autowired
    private DocumentImageRepository documentImageRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @MockitoBean
    private ImageStoragePort imageStoragePort;

    @BeforeEach
    void setUp() {
        documentImageRepository.deleteAll();
        documentRepository.deleteAll();
    }

    @DisplayName("이미지 목록을 업로드한다.")
    @Test
    void document_image_command_service_test_01() {
        // given
        final MockMultipartFile file1 = new MockMultipartFile(
            "file1",
            "test1.jpg",
            "multipart/form-data",
            "test1".getBytes()
        );
        final MockMultipartFile file2 = new MockMultipartFile(
            "file2",
            "test2.jpg",
            "multipart/form-data",
            "test2".getBytes()
        );

        // stub
        final UploadedImage uploadedImage1 = new UploadedImage("https://test.com/test1.jpg");
        final UploadedImage uploadedImage2 = new UploadedImage("https://test.com/test2.jpg");

        when(imageStoragePort.upload(file1)).thenReturn(uploadedImage1);
        when(imageStoragePort.upload(file2)).thenReturn(uploadedImage2);

        // when
        final DocumentImageUploadResponses response = documentImageCommandService.uploadImages(
            List.of(file1, file2),
            "127.0.0.1"
        );

        // then
        assertThat(response.images()).hasSize(2);
    }

    @DisplayName("만료기한이 지난 이미지들을 삭제한다.")
    @Test
    void document_image_command_service_test_02() {
        // given
        final Document document = Document.create(
            "제목",
            DocumentCategory.USER
        );
        documentRepository.save(document);

        final DocumentImage image1 = DocumentImage.create("http://test.com/image1", "127.0.0.1");
        final DocumentImage image2 = DocumentImage.create("http://test.com/image2", "127.0.0.1");

        ReflectionTestUtils.setField(image1, "expiredAt", LocalDateTime.now().minusDays(1));
        ReflectionTestUtils.setField(image2, "expiredAt", LocalDateTime.now().minusDays(1));

        documentImageRepository.saveAll(List.of(image1, image2));

        // when
        final long deleted = documentImageCommandService.deleteTempImages();

        // then
        assertThat(deleted).isEqualTo(2);
    }

    @DisplayName("이미지들을 문서에 사용한다.")
    @Test
    void document_image_command_service_test_03() {
        // given
        final Document document = Document.create(
            "제목",
            DocumentCategory.USER
        );
        documentRepository.save(document);

        final DocumentImage image1 = DocumentImage.create("http://test.com/image1", "127.0.0.1");
        final DocumentImage image2 = DocumentImage.create("http://test.com/image2", "127.0.0.1");
        documentImageRepository.saveAll(List.of(image1, image2));

        // when
        documentImageCommandService.attachTempImages(
            document,
            List.of(image1.getId(), image2.getId())
        );

        // then
        final DocumentImage foundImage1 = documentImageRepository.findAll().getFirst();
        final DocumentImage foundImage2 = documentImageRepository.findAll().getLast();

        assertThat(foundImage1.getStatus()).isEqualTo(DocumentImageStatus.USED);
        assertThat(foundImage2.getStatus()).isEqualTo(DocumentImageStatus.USED);
    }

    @DisplayName("이미지 삭제 도중 에러가 발생하는 경우 예외가 발생한다.")
    @Test
    void exception_document_image_command_service_test_01() {
        // given
        final Document document = Document.create(
            "제목",
            DocumentCategory.USER
        );
        documentRepository.save(document);

        final DocumentImage image1 = DocumentImage.create("http://test.com/image1", "127.0.0.1");
        final DocumentImage image2 = DocumentImage.create("http://test.com/image2", "127.0.0.1");

        ReflectionTestUtils.setField(image1, "expiredAt", LocalDateTime.now().minusDays(1));
        ReflectionTestUtils.setField(image2, "expiredAt", LocalDateTime.now().minusDays(1));
        documentImageRepository.saveAll(List.of(image1, image2));

        // stub
        doThrow(new RuntimeException()).when(imageStoragePort).delete(anyString());

        // when & then
        assertThatThrownBy(() -> documentImageCommandService.deleteTempImages())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("이미지 삭제 실패");
    }

    @DisplayName("업로드 하는 이미지와 문서에서 사용하는 이미지 갯수가 다를 경우 예외가 발생한다.")
    @Test
    void exception_document_image_command_service_test_02() {
        // given
        final Document document = Document.create(
            "제목",
            DocumentCategory.USER
        );
        documentRepository.save(document);

        final DocumentImage image1 = DocumentImage.create("http://test.com/image1", "127.0.0.1");
        final DocumentImage image2 = DocumentImage.create("http://test.com/image2", "127.0.0.1");

        ReflectionTestUtils.setField(image1, "status", DocumentImageStatus.USED);
        documentImageRepository.saveAll(List.of(image1, image2));

        // when & then
        assertThatThrownBy(() -> documentImageCommandService.attachTempImages(
            document,
            List.of(image1.getId(), image2.getId())
        ))
            .isInstanceOf(CustomException.class)
            .hasMessage("이미지 업로드 갯수가 올바르지 않습니다.");
    }
}