package com.openmpy.server.document.presentation;

import com.openmpy.server.document.application.DocumentQueryService;
import com.openmpy.server.document.application.DocumentRankingQueryService;
import com.openmpy.server.document.dto.response.DocumentGetResponse;
import com.openmpy.server.document.dto.response.DocumentHistoryPageResponse;
import com.openmpy.server.document.dto.response.DocumentPageResponse;
import com.openmpy.server.document.dto.response.DocumentTop10Response;
import com.openmpy.server.global.dto.CursorResponse;
import com.openmpy.server.global.dto.PageResponse;
import com.openmpy.server.global.dto.SliceResponse;
import com.openmpy.server.global.util.ClientIpResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class DocumentQueryController {

    private final DocumentQueryService documentQueryService;
    private final DocumentRankingQueryService documentRankingQueryService;

    @GetMapping("/v1/documents/{documentId}")
    public ResponseEntity<DocumentGetResponse> getLatest(
        @PathVariable final Long documentId,
        final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpResolver.getClientIp(servletRequest);
        final DocumentGetResponse response = documentQueryService.getLatest(documentId);

        documentRankingQueryService.increaseRankIfAllowed(documentId, clientIp);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/documents")
    public ResponseEntity<PageResponse<DocumentPageResponse>> getLatestDocumentsV1(
        @RequestParam(defaultValue = "all") final String category,
        @RequestParam(defaultValue = "0", required = false) final int page,
        @RequestParam(defaultValue = "10", required = false) final int size
    ) {
        return ResponseEntity.ok(documentQueryService.getLatestDocumentsV1(category, page, size));
    }

    @GetMapping("/v2/documents")
    public ResponseEntity<PageResponse<DocumentPageResponse>> getLatestDocumentsV2(
        @RequestParam(defaultValue = "all") final String category,
        @RequestParam(defaultValue = "0", required = false) final int page,
        @RequestParam(defaultValue = "10", required = false) final int size
    ) {
        return ResponseEntity.ok(documentQueryService.getLatestDocumentsV2(category, page, size));
    }

    @GetMapping("/v1/document-histories/{historyId}")
    public ResponseEntity<DocumentGetResponse> getHistory(@PathVariable final Long historyId) {
        return ResponseEntity.ok(documentQueryService.getHistory(historyId));
    }

    @GetMapping("/v1/documents/{documentId}/histories")
    public ResponseEntity<PageResponse<DocumentHistoryPageResponse>> getHistories(
        @PathVariable final Long documentId,
        @RequestParam(defaultValue = "0", required = false) final int page,
        @RequestParam(defaultValue = "10", required = false) final int size
    ) {
        return ResponseEntity.ok(documentQueryService.getHistories(documentId, page, size));
    }

    @GetMapping("/v1/documents/search")
    public ResponseEntity<SliceResponse<DocumentPageResponse>> searchDocumentsV1(
        @RequestParam final String keyword,
        @RequestParam(defaultValue = "0", required = false) final int page,
        @RequestParam(defaultValue = "10", required = false) final int size
    ) {
        return ResponseEntity.ok(documentQueryService.searchDocumentsV1(keyword, page, size));
    }

    @GetMapping("/v2/documents/search")
    public ResponseEntity<CursorResponse<DocumentPageResponse>> searchDocumentsV2(
        @RequestParam final String keyword,
        @RequestParam(required = false) final Long cursorId,
        @RequestParam(defaultValue = "10", required = false) final int size
    ) {
        return ResponseEntity.ok(documentQueryService.searchDocumentsV2(keyword, cursorId, size));
    }

    @GetMapping("/v1/documents/top10")
    public ResponseEntity<DocumentTop10Response> getDocumentTop10() {
        final DocumentTop10Response response = documentRankingQueryService.getDocumentTop10();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/documents/shuffle")
    public ResponseEntity<DocumentPageResponse> getShuffleDocument() {
        final DocumentPageResponse response = documentQueryService.getShuffleDocument();
        return ResponseEntity.ok(response);
    }
}
