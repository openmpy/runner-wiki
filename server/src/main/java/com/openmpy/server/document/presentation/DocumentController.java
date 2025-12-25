package com.openmpy.server.document.presentation;

import com.openmpy.server.document.application.DocumentService;
import com.openmpy.server.document.application.request.DocumentCreateRequest;
import com.openmpy.server.document.application.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.response.DocumentCreateResponse;
import com.openmpy.server.document.application.response.DocumentGetResponse;
import com.openmpy.server.document.application.response.DocumentPageResponse;
import com.openmpy.server.document.application.response.DocumentUpdateResponse;
import com.openmpy.server.global.dto.response.PageResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/documents")
    public ResponseEntity<DocumentCreateResponse> create(
            @RequestBody final DocumentCreateRequest request,
            final HttpServletRequest servletRequest
    ) {
        return ResponseEntity.ok(documentService.create(request, servletRequest));
    }

    @PutMapping("/documents/{documentId}")
    public ResponseEntity<DocumentUpdateResponse> update(
            @PathVariable final Long documentId,
            @RequestBody final DocumentUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        return ResponseEntity.ok(documentService.update(documentId, request, servletRequest));
    }

    @GetMapping("/documents/{documentId}")
    public ResponseEntity<DocumentGetResponse> getLatest(@PathVariable final Long documentId) {
        return ResponseEntity.ok(documentService.getLatest(documentId));
    }

    @GetMapping("/documents")
    public ResponseEntity<PageResponse<DocumentPageResponse>> getLatestDocuments(
            @RequestParam(defaultValue = "all") final String category,
            @RequestParam(defaultValue = "0", required = false) final int page,
            @RequestParam(defaultValue = "10", required = false) final int size
    ) {
        return ResponseEntity.ok(documentService.getLatestDocuments(category, page, size));
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> delete(@PathVariable final Long documentId) {
        documentService.delete(documentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/document-histories/{historyId}")
    public ResponseEntity<Void> deleteHistory(@PathVariable final Long historyId) {
        documentService.deleteHistory(historyId);
        return ResponseEntity.ok().build();
    }
}
