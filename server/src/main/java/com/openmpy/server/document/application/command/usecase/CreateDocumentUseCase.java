package com.openmpy.server.document.application.command.usecase;

import com.openmpy.server.document.application.command.dto.request.DocumentCreateRequest;
import com.openmpy.server.document.application.command.dto.response.DocumentCreateResponse;
import com.openmpy.server.document.application.support.ContentSizeCalculator;
import com.openmpy.server.document.application.support.ImageAttacher;
import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateDocumentUseCase {

    private final DocumentRepository documentRepository;

    private final ImageAttacher imageAttacher;
    private final ContentSizeCalculator contentSizeCalculator;

    @Transactional
    public DocumentCreateResponse execute(
            final DocumentCreateRequest request,
            final String clientIp
    ) {
        validateDuplicate(request);

        final Document document = Document.create(request.title(), request.category());
        final Document savedDocument = documentRepository.save(document);

        savedDocument.addHistory(
                request.author(),
                request.content(),
                contentSizeCalculator.calculateUtf8Bytes(request.content()),
                clientIp
        );
        imageAttacher.attachTempImages(savedDocument, request.imageIds());
        return new DocumentCreateResponse(savedDocument.getId());
    }

    private void validateDuplicate(final DocumentCreateRequest request) {
        if (documentRepository.existsByTitle_ValueAndCategory(request.title(), request.category())) {
            throw new CustomException("이미 작성된 문서입니다.");
        }
    }
}
