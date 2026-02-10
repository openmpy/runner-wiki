package com.openmpy.server.document.application.ranking.service;

import static java.util.HashMap.newHashMap;

import com.openmpy.server.document.application.query.dto.response.DocumentPageResponse;
import com.openmpy.server.document.application.query.dto.response.DocumentTop10Response;
import com.openmpy.server.document.application.ranking.port.DocumentRankingPort;
import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentRankingQueryService {

    private static final int MAX_TOP_DOCUMENTS = 10;

    private final DocumentRepository documentRepository;
    private final DocumentRankingPort rankingPort;

    public void increaseRankIfAllowed(final Long documentId, final String ip) {
        rankingPort.increaseRankIfAllowed(documentId, ip);
    }

    @Transactional(readOnly = true)
    public DocumentTop10Response getDocumentTop10() {
        final String rankKey = rankingPort.rankKeyOfToday();

        final int fetchSize = 80;
        final Set<String> rawIds = rankingPort.getTopRankedIds(fetchSize);

        if (rawIds == null || rawIds.isEmpty()) {
            return new DocumentTop10Response(List.of());
        }

        final List<Long> rankedIds = rawIds.stream().map(Long::parseLong).toList();
        final List<Document> documents = documentRepository.findAllByIdIn(rankedIds);

        final Map<Long, Integer> orderIndex = newHashMap(rankedIds.size() * 2);
        for (int i = 0; i < rankedIds.size(); i++) {
            orderIndex.put(rankedIds.get(i), i);
        }

        final List<Document> sorted = documents.stream()
            .sorted(
                Comparator.comparingInt(d -> orderIndex.getOrDefault(d.getId(), Integer.MAX_VALUE)))
            .limit(MAX_TOP_DOCUMENTS)
            .toList();

        cleanupInvalidMembers(rankKey, rankedIds, documents);

        final List<DocumentPageResponse> responses = sorted.stream()
            .map(DocumentPageResponse::from)
            .toList();

        return new DocumentTop10Response(responses);
    }

    private void cleanupInvalidMembers(
        final String rankKey,
        final List<Long> rankedIds,
        final List<Document> foundDocuments
    ) {
        final Set<Long> foundIds = foundDocuments.stream()
            .map(Document::getId)
            .collect(Collectors.toSet());

        final List<String> invalidMembers = rankedIds.stream()
            .filter(id -> !foundIds.contains(id))
            .limit(30)
            .map(String::valueOf)
            .toList();

        if (!invalidMembers.isEmpty()) {
            rankingPort.removeInvalidMembers(rankKey, invalidMembers.toArray());
        }
    }
}
