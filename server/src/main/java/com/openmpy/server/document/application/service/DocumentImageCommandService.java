package com.openmpy.server.document.application.service;

import com.openmpy.server.document.application.port.ImageStorage;
import com.openmpy.server.document.application.port.UploadedImage;
import com.openmpy.server.document.application.response.DocumentImageUploadResponse;
import com.openmpy.server.document.application.response.DocumentImageUploadResponses;
import com.openmpy.server.document.domain.entity.DocumentImage;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import com.openmpy.server.global.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class DocumentImageCommandService {

    private final DocumentImageRepository documentImageRepository;
    private final ImageStorage imageStorage;

    @Transactional
    public DocumentImageUploadResponses uploadImages(
            final List<MultipartFile> images,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpUtil.getClientIp(servletRequest);
        final List<DocumentImageUploadResponse> responses = images.stream()
                .map(it -> {
                    final UploadedImage uploadedImage = imageStorage.upload(it);
                    final DocumentImage documentImage = DocumentImage.create(uploadedImage.url(), clientIp);
                    final DocumentImage savedDocumentImage = documentImageRepository.save(documentImage);

                    return new DocumentImageUploadResponse(savedDocumentImage.getId(), uploadedImage.url());
                })
                .toList();

        return new DocumentImageUploadResponses(responses);
    }
}
