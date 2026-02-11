package com.openmpy.server.document.domain.repository.querydsl;

import static com.openmpy.server.document.domain.entity.QDocument.document;
import static com.openmpy.server.document.domain.entity.QDocumentHistory.documentHistory;

import com.openmpy.server.document.dto.response.DocumentGetResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DocumentRepositoryImpl implements DocumentCustomRepository {

    private final JPQLQueryFactory query;

    @Override
    public DocumentGetResponse findLatestDocumentById(final Long documentId) {
        return query
            .select(Projections.constructor(
                DocumentGetResponse.class,
                document.id,
                documentHistory.id,
                document.title.value,
                document.category,
                documentHistory.author.value,
                documentHistory.content.value,
                documentHistory.version.value,
                documentHistory.size.value,
                document.createdAt,
                documentHistory.createdAt
            ))
            .from(documentHistory)
            .join(documentHistory.document, document)
            .where(document.id.eq(documentId))
            .orderBy(documentHistory.version.value.desc())
            .limit(1)
            .fetchOne();
    }
}
