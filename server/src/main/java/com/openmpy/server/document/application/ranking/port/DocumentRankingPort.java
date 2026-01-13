package com.openmpy.server.document.application.ranking.port;

import java.util.Set;

public interface DocumentRankingPort {

    void removeFromRanking(final Long documentId);

    void increaseRankIfAllowed(final Long documentId, final String ip);

    Set<String> getTopRankedIds(final int fetchSize);

    void removeInvalidMembers(final String rankKey, final Object[] members);

    String rankKeyOfToday();

    String duplicatedBlockKey(final Long documentId, final String ip);
}
