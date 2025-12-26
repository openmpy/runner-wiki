package com.openmpy.server.document.presentation;

import com.openmpy.server.document.application.command.DocumentCommandService;
import com.openmpy.server.document.application.command.DocumentImageCommandService;
import com.openmpy.server.document.application.command.request.DocumentCreateRequest;
import com.openmpy.server.document.application.command.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.command.response.DocumentCreateResponse;
import com.openmpy.server.document.application.command.response.DocumentImageUploadResponses;
import com.openmpy.server.document.application.command.response.DocumentUpdateResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class DocumentCommandController {

    private final DocumentCommandService documentCommandService;
    private final DocumentImageCommandService documentImageCommandService;

    @PostMapping("/documents")
    public ResponseEntity<DocumentCreateResponse> create(
            @RequestBody final DocumentCreateRequest request,
            final HttpServletRequest servletRequest
    ) {
        return ResponseEntity.ok(documentCommandService.create(request, servletRequest));
    }

    @PutMapping("/documents/{documentId}")
    public ResponseEntity<DocumentUpdateResponse> update(
            @PathVariable final Long documentId,
            @RequestBody final DocumentUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        return ResponseEntity.ok(documentCommandService.update(documentId, request, servletRequest));
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> delete(@PathVariable final Long documentId) {
        documentCommandService.delete(documentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/document-histories/{historyId}")
    public ResponseEntity<Void> deleteHistory(@PathVariable final Long historyId) {
        documentCommandService.deleteHistory(historyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/document-images")
    public ResponseEntity<DocumentImageUploadResponses> uploadImages(
            @RequestBody final List<MultipartFile> images,
            final HttpServletRequest servletRequest
    ) {
        final DocumentImageUploadResponses responses = documentImageCommandService.uploadImages(images, servletRequest);
        return ResponseEntity.ok(responses);
    }
}
