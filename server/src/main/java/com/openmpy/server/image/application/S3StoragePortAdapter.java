package com.openmpy.server.image.application;

import com.openmpy.server.global.exception.CustomException;
import com.openmpy.server.global.properties.S3Properties;
import com.openmpy.server.image.application.port.ImageStoragePort;
import com.openmpy.server.image.dto.ImagePresignRequest;
import com.openmpy.server.image.dto.ImagePresignResponse;
import java.net.URI;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Set;
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

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
        "image/jpeg",
        "image/png",
        "image/webp",
        "image/gif",
        "image/heic",
        "image/heif",
        "image/avif",
        "image/bmp",
        "image/tiff"
    );
    public static final String IMAGE_FOLDER = "image";
    public static final String TEMP_FOLDER = "temp";

    private final S3Properties s3Properties;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Override
    public ImagePresignResponse presign(final ImagePresignRequest request) {
        validateContentType(request.contentType());

        final String extension = normalizeExtension(request.contentType());
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
        final String key = extractKey(tempImageUrl);

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

        final Pattern compile = Pattern.compile(
            "https://" + s3Properties.bucket()
                + "\\.s3\\.ap-northeast-2\\.amazonaws\\.com/temp/([^\\s\"']+)"
        );
        final Matcher matcher = compile.matcher(content);
        final StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            final String fileName = matcher.group(1);
            final String newUrl = s3Properties.endpoint() + "/image/" + fileName;

            matcher.appendReplacement(sb, newUrl);
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    private void validateContentType(final String contentType) {
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new CustomException("지원하지 않는 Content Type 입니다.");
        }
    }

    private String normalizeExtension(final String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> "jpeg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            case "image/gif" -> "gif";
            case "image/heic" -> "heic";
            case "image/heif" -> "heif";
            case "image/avif" -> "avif";
            case "image/bmp" -> "bmp";
            case "image/tiff" -> "tiff";
            default -> "bin";
        };
    }

    private String extractKey(final String url) {
        try {
            final URI uri = URI.create(url);
            final String path = uri.getPath();

            if (path == null || path.isBlank()) {
                throw new CustomException("잘못된 S3 URL입니다.");
            }

            return Paths.get(path).getFileName().toString();
        } catch (final Exception e) {
            throw new CustomException("잘못된 이미지 URL입니다. " + url);
        }
    }
}
