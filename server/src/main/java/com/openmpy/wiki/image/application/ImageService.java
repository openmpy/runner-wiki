package com.openmpy.wiki.image.application;

import com.openmpy.wiki.global.exception.CustomException;
import com.openmpy.wiki.global.properties.S3Properties;
import com.openmpy.wiki.global.snowflake.Snowflake;
import com.openmpy.wiki.image.application.response.ImageUploadResponse;
import com.openmpy.wiki.image.domain.entity.Image;
import com.openmpy.wiki.image.domain.repository.ImageRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "png", "jpg", "jpeg", "gif", "webp", "bmp", "tiff", "tif", "heic", "svg", "avif", "ico"
    );
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final Snowflake snowflake = new Snowflake();
    private final ImageRepository imageRepository;
    private final S3Properties s3Properties;
    private final S3Client s3Client;

    @Transactional
    public ImageUploadResponse upload(final MultipartFile file, final String clientIp) {
        final String originalFilename = file.getOriginalFilename();
        final String extension = StringUtils.getFilenameExtension(originalFilename);

        validateExtension(extension);
        validateSize(file);

        try {
            final String key = UUID.randomUUID() + "__" + originalFilename;
            final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.bucket())
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            final Image image = imageRepository.save(Image.create(snowflake.nextId(), key, clientIp));
            return new ImageUploadResponse(image.getId(), key);
        } catch (final IOException e) {
            throw new CustomException("이미지 업로드 중에 에러가 발생했습니다. " + e.getMessage());
        }
    }

    @Transactional
    public void uses(final List<Long> imageIds, final String documentId) {
        if (imageIds != null && !imageIds.isEmpty()) {
            for (final Long imageId : imageIds) {
                imageRepository.findById(imageId).ifPresent(image -> image.markUsed(Long.parseLong(documentId)));
            }
        }
    }

    @Transactional
    public void delete(final Long documentId) {
        imageRepository.findAllByDocumentId(documentId).forEach(Image::markDeleted);
    }

    @Transactional
    public void cleanUp() {
        final LocalDateTime cutoffTime = LocalDateTime.now().minusHours(12);
        final List<Image> images = imageRepository.findAllByUsedFalseAndCreatedAtBefore(cutoffTime);

        for (final Image image : images) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(s3Properties.bucket())
                    .key(image.getName())
                    .build()
            );
        }
        imageRepository.deleteAll(images);
    }

    private void validateExtension(final String extension) {
        if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new CustomException("지원 하지 않는 확장자입니다.");
        }
    }

    private void validateSize(final MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CustomException("파일 사이즈가 10MB를 초과했습니다.");
        }
    }
}
