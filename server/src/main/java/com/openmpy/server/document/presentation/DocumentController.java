package com.openmpy.server.document.presentation;

import com.openmpy.server.document.application.DocumentService;
import com.openmpy.server.document.application.request.DocumentCreateRequest;
import com.openmpy.server.document.application.response.DocumentCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
