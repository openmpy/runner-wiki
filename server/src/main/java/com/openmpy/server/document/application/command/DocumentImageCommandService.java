package com.openmpy.server.document.application.command;

import com.openmpy.server.document.application.command.response.DocumentImageUploadResponses;
import com.openmpy.server.document.application.usecase.UploadDocumentImagesUseCase;
import jakarta.servlet.http.HttpServletRequest;
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
            final HttpServletRequest servletRequest
    ) {
        return uploadDocumentImagesUseCase.execute(images, servletRequest);
    }
}
