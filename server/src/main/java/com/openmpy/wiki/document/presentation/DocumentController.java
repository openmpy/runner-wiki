package com.openmpy.wiki.document.presentation;

import com.openmpy.wiki.document.application.DocumentFacade;
import com.openmpy.wiki.document.application.DocumentService;
import com.openmpy.wiki.document.application.request.DocumentCreateRequest;
import com.openmpy.wiki.document.application.request.DocumentUpdateRequest;
import com.openmpy.wiki.document.application.response.DocumentCreateResponse;
import com.openmpy.wiki.document.application.response.DocumentHistoryReadResponses;
import com.openmpy.wiki.document.application.response.DocumentReadResponse;
import com.openmpy.wiki.document.application.response.DocumentReadResponses;
import com.openmpy.wiki.document.application.response.DocumentUpdateResponse;
import com.openmpy.wiki.global.dto.PageResponse;
import com.openmpy.wiki.global.utils.ClientIpExtractor;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
@RestController
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentFacade documentFacade;

    @PostMapping
    public ResponseEntity<DocumentCreateResponse> createDocument(
            @RequestBody final DocumentCreateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        return ResponseEntity.ok(documentFacade.createDocument(request, clientIp));
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<DocumentUpdateResponse> updateDocument(
            @PathVariable final String documentId,
            @RequestBody final DocumentUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        return ResponseEntity.ok(documentFacade.updateDocument(documentId, request, clientIp));
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentReadResponse> readDocument(@PathVariable final String documentId) {
        return ResponseEntity.ok(documentService.readLatestDocument(documentId));
    }

    @GetMapping("/histories/{documentHistoryId}")
    public ResponseEntity<DocumentReadResponse> readDocumentHistory(@PathVariable final String documentHistoryId) {
        return ResponseEntity.ok(documentService.readDocumentHistory(documentHistoryId));
    }

    @GetMapping("/latest")
    public ResponseEntity<PageResponse<List<DocumentReadResponse>>> readLatestCreateDocuments(
            @RequestParam("page") final int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) final int size
    ) {
        return ResponseEntity.ok(documentService.readLatestDocuments(page, size));
    }

    @GetMapping("/{documentId}/histories")
    public ResponseEntity<PageResponse<DocumentHistoryReadResponses>> readDocumentHistory(
            @PathVariable final String documentId,
            @RequestParam("page") final int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
        return ResponseEntity.ok(documentService.readDocumentHistories(documentId, page, size));
    }

    @GetMapping("/popular")
    public ResponseEntity<DocumentReadResponses> readPopularDocuments() {
        final DocumentReadResponses responses = documentService.readPopularDocuments();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/random")
    public ResponseEntity<DocumentReadResponse> readRandomDocument() {
        final DocumentReadResponse response = documentService.readRandomDocument();
        return ResponseEntity.ok(response);
    }
}
