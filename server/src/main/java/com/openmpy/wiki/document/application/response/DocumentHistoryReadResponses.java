package com.openmpy.wiki.document.application.response;

import com.openmpy.wiki.document.domain.entity.Document;
import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import java.util.List;

public record DocumentHistoryReadResponses(
        String historyId,
        String title,
        List<DocumentHistoryReadResponse> histories
) {

    public static DocumentHistoryReadResponses from(
            final Document document, final List<DocumentHistory> documentHistory
    ) {
        return new DocumentHistoryReadResponses(
                document.getId(),
                document.getTitle(),
                documentHistory.stream().map(DocumentHistoryReadResponse::from).toList()
        );
    }
}
