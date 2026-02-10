package com.openmpy.server.document.application;

import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.repository.search.DocumentSearchRepository;
import com.openmpy.server.document.dto.response.DocumentPageResponse;
import com.openmpy.server.global.dto.PageResponse;
import com.openmpy.server.global.util.SearchInputClassifier;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentSearchQueryService {

    private final DocumentSearchRepository documentSearchRepository;

    public PageResponse<DocumentPageResponse> autoComplete(
        final String title,
        final int page,
        final int size
    ) {
        String query = title;

        if (query == null) {
            query = "";
        }
        query = query.trim();

        if (query.isBlank()) {
            return PageResponse.of(Collections.emptyList(), page, size, 0);
        }

        final Pageable pageable = PageRequest.of(page, size);
        Page<Document> result = null;

        if (SearchInputClassifier.isChosungQuery(query)) {
            result = documentSearchRepository.searchByChosungPrefix(query, pageable);
        }

        if (!SearchInputClassifier.isChosungQuery(query)) {
            final Page<Document> prefix = documentSearchRepository.searchByTitlePrefix(
                query,
                pageable
            );
            result = prefix;

            if (!prefix.hasContent()) {
                result = documentSearchRepository.searchByTrgm(query, pageable);
            }
        }

        final List<DocumentPageResponse> payload = Objects.requireNonNull(result).getContent()
            .stream()
            .map(DocumentPageResponse::from)
            .toList();

        return PageResponse.of(
            payload,
            result.getNumber(),
            result.getSize(),
            result.getTotalElements()
        );
    }
}
