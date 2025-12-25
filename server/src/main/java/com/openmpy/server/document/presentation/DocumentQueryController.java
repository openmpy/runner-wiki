package com.openmpy.server.document.presentation;

import com.openmpy.server.document.application.DocumentQueryService;
import com.openmpy.server.document.application.response.DocumentGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class DocumentQueryController {

    private final DocumentQueryService documentQueryService;

    @GetMapping("/document-histories/{historyId}")
    public ResponseEntity<DocumentGetResponse> getHistory(@PathVariable final Long historyId) {
        return ResponseEntity.ok(documentQueryService.getHistory(historyId));
    }
}
