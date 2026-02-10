package com.openmpy.server.document.application.command.usecase;

import com.openmpy.server.document.application.command.dto.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.command.dto.response.DocumentUpdateResponse;
import com.openmpy.server.document.application.support.ContentSizeCalculator;
import com.openmpy.server.document.application.support.ImageAttacher;
import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateDocumentUseCase {

    private final DocumentRepository documentRepository;

    private final ImageAttacher imageAttacher;
    private final ContentSizeCalculator contentSizeCalculator;

    @Transactional
    public DocumentUpdateResponse execute(
        final Long documentId,
        final DocumentUpdateRequest request,
        final String clientIp
    ) {
        final Document document = findDocumentById(documentId);

        document.addHistory(
            request.author(),
            request.content(),
            contentSizeCalculator.calculateUtf8Bytes(request.content()),
            clientIp
        );
        imageAttacher.attachTempImages(document, request.imageIds());
        return new DocumentUpdateResponse(document.getId());
    }

    private Document findDocumentById(final Long documentId) {
        return documentRepository.findById(documentId).orElseThrow(
            () -> new CustomException("찾을 수 없는 문서 번호입니다.")
        );
    }
}
