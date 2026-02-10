package com.openmpy.server.document.presentation;

import com.openmpy.server.document.application.DocumentQueryService;
import com.openmpy.server.document.application.DocumentRankingQueryService;
import com.openmpy.server.document.application.DocumentSearchQueryService;
import com.openmpy.server.document.dto.response.DocumentGetResponse;
import com.openmpy.server.document.dto.response.DocumentHistoryPageResponse;
import com.openmpy.server.document.dto.response.DocumentPageResponse;
import com.openmpy.server.document.dto.response.DocumentTop10Response;
import com.openmpy.server.global.dto.PageResponse;
import com.openmpy.server.global.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    private final DocumentRankingQueryService documentRankingQueryService;
    private final DocumentSearchQueryService documentSearchQueryService;

    @GetMapping("/documents/{documentId}")
    public ResponseEntity<DocumentGetResponse> getLatest(
        @PathVariable final Long documentId,
        final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpUtil.getClientIp(servletRequest);
        final DocumentGetResponse response = documentQueryService.getLatest(documentId);

        documentRankingQueryService.increaseRankIfAllowed(documentId, clientIp);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/documents")
    public ResponseEntity<PageResponse<DocumentPageResponse>> getLatestDocuments(
        @RequestParam(defaultValue = "all") final String category,
        @RequestParam(defaultValue = "0", required = false) final int page,
        @RequestParam(defaultValue = "10", required = false) final int size
    ) {
        return ResponseEntity.ok(documentQueryService.getLatestDocuments(category, page, size));
    }

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

    @GetMapping("/documents/search")
    public ResponseEntity<PageResponse<DocumentPageResponse>> searchDocuments(
        @RequestParam final String title,
        @RequestParam(defaultValue = "0", required = false) final int page,
        @RequestParam(defaultValue = "10", required = false) final int size
    ) {
        return ResponseEntity.ok(documentQueryService.searchDocuments(title, page, size));
    }

    @GetMapping("/documents/top10")
    public ResponseEntity<DocumentTop10Response> getDocumentTop10() {
        final DocumentTop10Response response = documentRankingQueryService.getDocumentTop10();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/documents/shuffle")
    public ResponseEntity<DocumentPageResponse> getShuffleDocument() {
        final DocumentPageResponse response = documentQueryService.getShuffleDocument();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/documents/autocomplete")
    public ResponseEntity<PageResponse<DocumentPageResponse>> autoComplete(
        @RequestParam final String title,
        @RequestParam(defaultValue = "0", required = false) final int page,
        @RequestParam(defaultValue = "10", required = false) final int size
    ) {
        return ResponseEntity.ok(documentSearchQueryService.autoComplete(title, page, size));
    }
}
