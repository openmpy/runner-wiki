package com.openmpy.wiki.search.application;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.IndexesQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.Searchable;
import com.openmpy.wiki.search.application.request.SearchAddRequest;
import com.openmpy.wiki.search.application.request.SearchCreateRequest;
import com.openmpy.wiki.search.application.request.SearchUpdateRequest;
import com.openmpy.wiki.search.application.response.SearchDocumentResponse;
import com.openmpy.wiki.search.application.response.SearchDocumentResponses;
import com.openmpy.wiki.search.application.response.SearchIndexResponse;
import com.openmpy.wiki.search.application.response.SearchIndexResponses;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {

    private final Client meilisearchClient;

    public void createIndex(final SearchCreateRequest request) {
        try {
            meilisearchClient.createIndex(request.uid(), request.primaryKey());
        } catch (final MeilisearchException e) {
            log.warn("검색 인덱스 생성에 실패했습니다. {}", request.uid(), e);
        }
    }

    public SearchIndexResponse getIndex(final String uid) {
        try {
            final Index index = meilisearchClient.getIndex(uid);
            return SearchIndexResponse.from(index);
        } catch (final MeilisearchException e) {
            log.warn("검색 인덱스 조회에 실패했습니다. {}", uid, e);
            return null;
        }
    }

    public SearchIndexResponses getIndexes() {
        try {
            final IndexesQuery indexesQuery = new IndexesQuery();
            final Results<Index> indexes = meilisearchClient.getIndexes(indexesQuery);
            final Index[] results = indexes.getResults();

            return SearchIndexResponses.of(Arrays.stream(results)
                    .map(SearchIndexResponse::from)
                    .toList()
            );
        } catch (final MeilisearchException e) {
            log.warn("검색 인덱스 목록 조회에 실패했습니다.", e);
            return null;
        }
    }

    public void updateIndex(final String uid, final SearchUpdateRequest request) {
        try {
            meilisearchClient.updateIndex(uid, request.primaryKey());
        } catch (final MeilisearchException e) {
            log.error("검색 인덱스 수정에 실패했습니다. {}", uid, e);
        }
    }

    public void deleteIndex(final String uid) {
        try {
            meilisearchClient.deleteIndex(uid);
        } catch (final MeilisearchException e) {
            log.error("검색 인덱스 삭제에 실패했습니다. {}", uid, e);
        }
    }

    public void addDocument(final String uid, final SearchAddRequest request) {
        try {
            final JSONArray jsonArray = new JSONArray();
            final List<JSONObject> items = List.of(new JSONObject()
                    .put("id", request.documentId())
                    .put("title", request.title())
                    .put("category", request.category())
            );
            jsonArray.put(items);

            final String documents = jsonArray.getJSONArray(0).toString();
            final Index index = meilisearchClient.index(uid);
            index.addDocuments(documents);
        } catch (final MeilisearchException e) {
            log.warn("검색 인덱스에 문서 추가를 하지 못했습니다. {}", uid, e);
        }
    }

    public SearchDocumentResponses getDocuments(final String uid) {
        try {
            final Results<SearchDocumentResponse> documents = meilisearchClient.index(uid)
                    .getDocuments(SearchDocumentResponse.class);
            return SearchDocumentResponses.of(Arrays.asList(documents.getResults()));
        } catch (final MeilisearchException e) {
            log.warn("검색 인덱스에서 문서 목록 조회에 실패했습니다. {}", uid, e);
            return null;
        }
    }

    public void deleteDocument(final String uid, final String documentId) {
        try {
            meilisearchClient.index(uid).deleteDocument(documentId);
        } catch (final MeilisearchException e) {
            log.warn("검색 인덱스에 문서 삭제를 하지 못했습니다. {}", uid, e);
        }
    }

    public Searchable search(final String uid, final String query) {
        try {
            final Index index = meilisearchClient.index(uid);
            final SearchRequest searchRequest = SearchRequest.builder()
                    .q(query)
                    .attributesToSearchOn(new String[]{"title"})
                    .build();

            return index.search(searchRequest);
        } catch (final MeilisearchException e) {
            log.warn("검색에 실패했습니다. 쿼리 = {}", query, e);
            return null;
        }
    }
}
