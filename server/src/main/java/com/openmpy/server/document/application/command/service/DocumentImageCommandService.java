package com.openmpy.server.document.application.command.service;

import com.openmpy.server.document.application.command.dto.response.DocumentImageUploadResponses;
import com.openmpy.server.document.application.command.usecase.UploadDocumentImagesUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class DocumentImageCommandService {

    private final UploadDocumentImagesUseCase uploadDocumentImagesUseCase;

    @Transactional
    public DocumentImageUploadResponses uploadImages(
            final List<MultipartFile> images,
            final String clientIp
    ) {
        return uploadDocumentImagesUseCase.execute(images, clientIp);
    }
}
