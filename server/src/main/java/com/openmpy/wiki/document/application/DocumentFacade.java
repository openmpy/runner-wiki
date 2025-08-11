package com.openmpy.wiki.document.application;

import com.openmpy.wiki.document.application.request.DocumentCreateRequest;
import com.openmpy.wiki.document.application.request.DocumentUpdateRequest;
import com.openmpy.wiki.document.application.response.DocumentCreateResponse;
import com.openmpy.wiki.document.application.response.DocumentUpdateResponse;
import com.openmpy.wiki.image.application.ImageService;
import com.openmpy.wiki.search.application.SearchService;
import com.openmpy.wiki.search.application.request.SearchAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentFacade {

    private final DocumentService documentService;
    private final ImageService imageService;
    private final SearchService searchService;

    @Transactional
    public DocumentCreateResponse createDocument(final DocumentCreateRequest request, final String clientIp) {
        final DocumentCreateResponse response = documentService.createDocument(request, clientIp);
        imageService.uses(request.imageIds(), response.documentId());
        addSearchDocument(request, response);
        return response;
    }

    @Transactional
    public DocumentUpdateResponse updateDocument(
            final String documentId, final DocumentUpdateRequest request, final String clientIp
    ) {
        final DocumentUpdateResponse response = documentService.updateDocument(documentId, request, clientIp);
        imageService.uses(request.imageIds(), documentId);
        return response;
    }

    @Transactional
    public void deleteDocument(final String documentId) {
        documentService.deleteDocument(documentId);
        imageService.delete(documentId);
        searchService.deleteDocument("document", documentId);
    }

    private void addSearchDocument(final DocumentCreateRequest request, final DocumentCreateResponse response) {
        final SearchAddRequest addRequest = new SearchAddRequest(
                response.documentId(), request.title(), request.category()
        );
        searchService.addDocument("document", addRequest);
    }
}
