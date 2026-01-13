package com.openmpy.server.document.application.ranking.service;

import com.openmpy.server.document.application.ranking.port.DocumentRankingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentRankingCommandService {

    private final DocumentRankingPort documentRankingPort;

    public void removeFromRanking(final Long documentId) {
        documentRankingPort.removeFromRanking(documentId);
    }
}
