package com.openmpy.server.document.presentation;

import com.openmpy.server.document.application.response.DocumentGetResponse;
import com.openmpy.server.document.application.response.DocumentHistoryPageResponse;
import com.openmpy.server.document.application.service.DocumentQueryService;
import com.openmpy.server.global.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/documents/{documentId}/histories")
    public ResponseEntity<PageResponse<DocumentHistoryPageResponse>> getHistories(
            @PathVariable final Long documentId,
            @RequestParam(defaultValue = "0", required = false) final int page,
            @RequestParam(defaultValue = "10", required = false) final int size
    ) {
        return ResponseEntity.ok(documentQueryService.getHistories(documentId, page, size));
    }
}
