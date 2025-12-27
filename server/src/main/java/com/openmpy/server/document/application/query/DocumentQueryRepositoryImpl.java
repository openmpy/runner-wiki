package com.openmpy.server.document.application.query;

import static com.openmpy.server.document.domain.model.QDocument.document;
import static com.openmpy.server.document.domain.model.QDocumentHistory.documentHistory;

import com.openmpy.server.document.application.query.response.DocumentGetResponse;
import com.openmpy.server.global.exception.CustomException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DocumentQueryRepositoryImpl implements DocumentQueryRepository {

    private final JPQLQueryFactory query;

    @Override
    public DocumentGetResponse findLatestDocumentById(final Long documentId) {
        final Long latestHistoryId = query
                .select(documentHistory.id)
                .from(documentHistory)
                .where(documentHistory.document.id.eq(documentId))
                .orderBy(documentHistory.version.desc())
                .limit(1)
                .fetchOne();

        if (latestHistoryId == null) {
            throw new CustomException("문서 또는 문서 기록이 존재하지 않습니다.");
        }

        return query
                .select(Projections.constructor(
                        DocumentGetResponse.class,
                        document.id,
                        documentHistory.id,
                        document.title.value,
                        document.category,
                        documentHistory.author.value,
                        documentHistory.content.value,
                        documentHistory.version,
                        documentHistory.size,
                        document.createdAt,
                        documentHistory.createdAt
                ))
                .from(document)
                .join(documentHistory).on(documentHistory.document.eq(document))
                .where(
                        document.id.eq(documentId),
                        documentHistory.id.eq(latestHistoryId)
                )
                .fetchOne();
    }
}
