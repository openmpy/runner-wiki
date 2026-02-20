package com.openmpy.server.image.application;

import com.openmpy.server.global.exception.CustomException;
import com.openmpy.server.global.properties.S3Properties;
import com.openmpy.server.image.application.port.ImageStoragePort;
import com.openmpy.server.image.dto.ImagePresignRequest;
import com.openmpy.server.image.dto.ImagePresignResponse;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@RequiredArgsConstructor
@Component
public class S3StoragePortAdapter implements ImageStoragePort {

    private static final Map<String, String> CONTENT_TYPE_TO_EXTENSION = Map.of(
        "image/jpeg", "jpeg",
        "image/png", "png",
        "image/webp", "webp",
        "image/gif", "gif",
        "image/heic", "heic",
        "image/heif", "heif",
        "image/avif", "avif",
        "image/bmp", "bmp",
        "image/tiff", "tiff"
    );
    public static final String IMAGE_FOLDER = "image";
    public static final String TEMP_FOLDER = "temp";

    private final S3Properties s3Properties;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Override
    public ImagePresignResponse presign(final ImagePresignRequest request) {
        validateContentType(request.contentType());

        final String extension = CONTENT_TYPE_TO_EXTENSION.getOrDefault(
            request.contentType(),
            "bin"
        );
        final String key = UUID.randomUUID() + "." + extension;

        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(TEMP_FOLDER)
            .key(key)
            .contentType(request.contentType())
            .build();
        final PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
            .putObjectRequest(putObjectRequest)
            .signatureDuration(Duration.ofSeconds(s3Properties.presignExpireSecond()))
            .build();
        final PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(
            putObjectPresignRequest
        );

        return new ImagePresignResponse(
            presignedPutObjectRequest.url().toString(),
            s3Properties.endpoint() + "/" + TEMP_FOLDER + "/" + key
        );
    }

    @Override
    public void useImage(final String tempImageUrl) {
        final String key = extractFileName(tempImageUrl);

        s3Client.copyObject(it -> it.sourceBucket(s3Properties.bucket())
            .sourceKey(TEMP_FOLDER + "/" + key)
            .destinationBucket(IMAGE_FOLDER)
            .destinationKey(key)
        );
        s3Client.deleteObject(it -> it.bucket(TEMP_FOLDER)
            .key(key)
        );
    }

    @Override
    public String convertTempToImageUrl(final String content) {
        if (content == null || content.isBlank()) {
            return content;
        }

        final Pattern pattern = Pattern.compile(
            "https://" + Pattern.quote(s3Properties.bucket())
                + "\\.s3\\.ap-northeast-2\\.amazonaws\\.com/temp/([^\\s)\"']+)"
        );

        final Matcher matcher = pattern.matcher(content);
        final StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            final String fileName = matcher.group(1);
            final String newUrl = s3Properties.endpoint() + "/image/" + fileName;

            matcher.appendReplacement(sb, Matcher.quoteReplacement(newUrl));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    private void validateContentType(final String contentType) {
        if (!CONTENT_TYPE_TO_EXTENSION.containsKey(contentType)) {
            throw new CustomException("지원하지 않는 Content Type 입니다.");
        }
    }

    private String extractFileName(final String url) {
        final int lastSlash = url.lastIndexOf('/');

        if (lastSlash == -1) {
            throw new CustomException("잘못된 URL 형식입니다.");
        }

        final String fileName = url.substring(lastSlash + 1);
        final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);

        if (!CONTENT_TYPE_TO_EXTENSION.containsValue(extension)) {
            throw new CustomException("잘못된 키 값입니다.");
        }
        return fileName;
    }
}
