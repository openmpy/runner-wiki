package com.openmpy.server.document.application;

import static com.openmpy.server.document.domain.type.DocumentImageStatus.TEMP;

import com.openmpy.server.document.application.image.dto.UploadedImage;
import com.openmpy.server.document.application.image.port.ImageStorage;
import com.openmpy.server.document.domain.entity.DocumentImage;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import com.openmpy.server.document.dto.response.DocumentImageUploadResponse;
import com.openmpy.server.document.dto.response.DocumentImageUploadResponses;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class DocumentImageCommandService {

    private final DocumentImageRepository documentImageRepository;
    private final ImageStorage imageStorage;

    @Transactional
    public DocumentImageUploadResponses uploadImages(
        final List<MultipartFile> images,
        final String clientIp
    ) {
        final List<DocumentImageUploadResponse> responses = images.stream()
            .map(it -> {
                final UploadedImage uploadedImage = imageStorage.upload(it);
                final DocumentImage documentImage = DocumentImage.create(
                    uploadedImage.url(),
                    clientIp
                );

                documentImageRepository.save(documentImage);
                return new DocumentImageUploadResponse(documentImage.getId(), uploadedImage.url());
            })
            .toList();

        return new DocumentImageUploadResponses(responses);
    }

    @Transactional
    public long deleteTempImages() {
        final List<DocumentImage> documentImages = documentImageRepository.findAllByStatusAndExpiredAtBefore(
            TEMP,
            LocalDateTime.now()
        );
        long deleted = 0;

        for (final DocumentImage documentImage : documentImages) {
            try {
                imageStorage.delete(documentImage.getUrl());
                documentImageRepository.delete(documentImage);
                deleted++;
            } catch (final Exception e) {
                throw new IllegalStateException("이미지 삭제 실패", e);
            }
        }
        return deleted;
    }
}
