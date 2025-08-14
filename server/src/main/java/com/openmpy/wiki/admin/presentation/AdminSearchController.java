package com.openmpy.wiki.admin.presentation;

import com.openmpy.wiki.search.application.SearchService;
import com.openmpy.wiki.search.application.request.SearchAddRequest;
import com.openmpy.wiki.search.application.request.SearchCreateRequest;
import com.openmpy.wiki.search.application.request.SearchUpdateRequest;
import com.openmpy.wiki.search.application.response.SearchDocumentResponses;
import com.openmpy.wiki.search.application.response.SearchIndexResponse;
import com.openmpy.wiki.search.application.response.SearchIndexResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/search")
@RestController
public class AdminSearchController {

    private final SearchService searchService;

    @PostMapping("/index")
    public ResponseEntity<Void> index(@RequestBody final SearchCreateRequest request) {
        searchService.createIndex(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/index/{uid}")
    public ResponseEntity<SearchIndexResponse> getIndex(@PathVariable final String uid) {
        final SearchIndexResponse response = searchService.getIndex(uid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/indexes")
    public ResponseEntity<SearchIndexResponses> getIndexes() {
        final SearchIndexResponses responses = searchService.getIndexes();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/index/{uid}")
    public ResponseEntity<Void> updateIndex(
            @PathVariable final String uid,
            @RequestBody final SearchUpdateRequest request
    ) {
        searchService.updateIndex(uid, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/index/{uid}")
    public ResponseEntity<Void> deleteIndex(@PathVariable final String uid) {
        searchService.deleteIndex(uid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/indexes/{uid}/documents")
    public ResponseEntity<Void> addDocument(
            @PathVariable final String uid,
            @RequestBody final SearchAddRequest request
    ) {
        searchService.addDocument(uid, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/indexes/{uid}/documents")
    public ResponseEntity<SearchDocumentResponses> getDocuments(@PathVariable final String uid) {
        final SearchDocumentResponses responses = searchService.getDocuments(uid);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/indexes/{uid}/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable final String uid,
            @PathVariable final String documentId
    ) {
        searchService.deleteDocument(uid, documentId);
        return ResponseEntity.noContent().build();
    }
}
