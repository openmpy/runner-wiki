package com.openmpy.server.document.presentation;

import com.openmpy.server.document.application.DocumentService;
import com.openmpy.server.document.application.request.DocumentCreateRequest;
import com.openmpy.server.document.application.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.response.DocumentCreateResponse;
import com.openmpy.server.document.application.response.DocumentGetResponse;
import com.openmpy.server.document.application.response.DocumentUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DocumentCreateResponse> create(@RequestBody final DocumentCreateRequest request) {
        return ResponseEntity.ok(documentService.create(request));
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<DocumentUpdateResponse> update(
            @PathVariable final Long documentId,
            @RequestBody final DocumentUpdateRequest request
    ) {
        return ResponseEntity.ok(documentService.update(documentId, request));
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentGetResponse> getLatest(@PathVariable final Long documentId) {
        return ResponseEntity.ok(documentService.getLatest(documentId));
    }
}
