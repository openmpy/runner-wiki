package com.openmpy.wiki.document.application;

import com.openmpy.wiki.document.application.request.DocumentCreateRequest;
import com.openmpy.wiki.document.application.request.DocumentUpdateRequest;
import com.openmpy.wiki.document.application.response.DocumentCreateResponse;
import com.openmpy.wiki.document.application.response.DocumentUpdateResponse;
import com.openmpy.wiki.image.application.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentFacade {

    private final DocumentService documentService;
    private final ImageService imageService;

    @Transactional
    public DocumentCreateResponse createDocument(final DocumentCreateRequest request, final String clientIp) {
        final DocumentCreateResponse response = documentService.createDocument(request, clientIp);
        imageService.uses(request.imageIds(), response.documentId());
        return response;
    }

    @Transactional
    public DocumentUpdateResponse updateDocument(
            final Long documentId, final DocumentUpdateRequest request, final String clientIp
    ) {
        final DocumentUpdateResponse response = documentService.updateDocument(documentId, request, clientIp);
        imageService.uses(request.imageIds(), documentId.toString());
        return response;
    }

    @Transactional
    public void deleteDocument(final Long documentId) {
        documentService.deleteDocument(documentId);
        imageService.delete(documentId);
    }
}
