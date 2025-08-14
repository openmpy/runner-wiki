package com.openmpy.wiki.search.presentation;

import com.meilisearch.sdk.model.Searchable;
import com.openmpy.wiki.search.application.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/document")
    public ResponseEntity<Searchable> search(
            @RequestParam final String uid, @RequestParam final String query
    ) {
        final Searchable response = searchService.searchDocument(uid, query);
        return ResponseEntity.ok(response);
    }
}
