package com.openmpy.server.document.application;

import com.openmpy.server.document.application.command.dto.response.DocumentImageUploadResponse;
import com.openmpy.server.document.application.command.dto.response.DocumentImageUploadResponses;
import com.openmpy.server.document.application.image.dto.UploadedImage;
import com.openmpy.server.document.application.image.port.ImageStorage;
import com.openmpy.server.document.domain.entity.DocumentImage;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import jakarta.transaction.Transactional;
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
}
