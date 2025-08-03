package com.openmpy.wiki.document.presentation;

import com.openmpy.wiki.document.application.DocumentService;
import com.openmpy.wiki.document.application.request.DocumentCreateRequest;
import com.openmpy.wiki.document.application.request.DocumentUpdateRequest;
import com.openmpy.wiki.document.application.response.DocumentCreateResponse;
import com.openmpy.wiki.document.application.response.DocumentReadResponse;
import com.openmpy.wiki.document.application.response.DocumentUpdateResponse;
import com.openmpy.wiki.global.utils.IpAddressUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
@RestController
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<DocumentCreateResponse> createDocument(
            @RequestBody final DocumentCreateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = IpAddressUtil.getClientIp(servletRequest);
        return ResponseEntity.ok(documentService.createDocument(request, clientIp));
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<DocumentUpdateResponse> updateDocument(
            @PathVariable final Long documentId,
            @RequestBody final DocumentUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = IpAddressUtil.getClientIp(servletRequest);
        return ResponseEntity.ok(documentService.updateDocument(documentId, request, clientIp));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable final Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/histories/{documentHistoryId}")
    public ResponseEntity<Void> deleteDocumentHistory(@PathVariable final Long documentHistoryId) {
        documentService.deleteDocumentHistory(documentHistoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentReadResponse> readDocument(@PathVariable final Long documentId) {
        return ResponseEntity.ok(documentService.readLatestDocument(documentId));
    }
}
