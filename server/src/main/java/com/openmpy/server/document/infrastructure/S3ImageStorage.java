package com.openmpy.server.document.infrastructure;

import com.openmpy.server.document.application.port.ImageStorage;
import com.openmpy.server.document.application.port.UploadedImage;
import com.openmpy.server.global.properties.S3Properties;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Component
public class S3ImageStorage implements ImageStorage {

    private final S3Properties s3Properties;
    private final S3Client s3Client;

    @Override
    public UploadedImage upload(final MultipartFile file) {
        try {
            final String key = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(s3Properties.bucket())
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return new UploadedImage(s3Properties.endpoint() + "/" + s3Properties.bucket() + "/" + key);
        } catch (Exception e) {
            throw new IllegalStateException("이미지 업로드 실패", e);
        }
    }
}
