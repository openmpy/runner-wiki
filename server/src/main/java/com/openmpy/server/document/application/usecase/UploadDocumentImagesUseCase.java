package com.openmpy.server.document.application.usecase;

import com.openmpy.server.document.application.command.response.DocumentImageUploadResponse;
import com.openmpy.server.document.application.command.response.DocumentImageUploadResponses;
import com.openmpy.server.document.application.port.ImageStorage;
import com.openmpy.server.document.application.port.UploadedImage;
import com.openmpy.server.document.domain.model.DocumentImage;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import com.openmpy.server.global.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UploadDocumentImagesUseCase {

    private final DocumentImageRepository documentImageRepository;
    private final ImageStorage imageStorage;

    @Transactional
    public DocumentImageUploadResponses execute(
            final List<MultipartFile> images,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpUtil.getClientIp(servletRequest);

        final List<DocumentImageUploadResponse> responses = images.stream()
                .map(file -> {
                    final UploadedImage uploadedImage = imageStorage.upload(file);
                    final DocumentImage documentImage = DocumentImage.create(uploadedImage.url(), clientIp);
                    final DocumentImage saved = documentImageRepository.save(documentImage);
                    return new DocumentImageUploadResponse(saved.getId(), uploadedImage.url());
                })
                .toList();

        return new DocumentImageUploadResponses(responses);
    }
}
