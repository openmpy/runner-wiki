package com.openmpy.wiki.document.application;

import com.openmpy.wiki.document.application.request.DocumentCreateRequest;
import com.openmpy.wiki.document.application.request.DocumentUpdateRequest;
import com.openmpy.wiki.document.application.response.DocumentCreateResponse;
import com.openmpy.wiki.document.application.response.DocumentReadResponse;
import com.openmpy.wiki.document.application.response.DocumentUpdateResponse;
import com.openmpy.wiki.document.domain.constants.DocumentCategory;
import com.openmpy.wiki.document.domain.entity.Document;
import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import com.openmpy.wiki.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.wiki.document.domain.repository.DocumentRepository;
import com.openmpy.wiki.global.exception.CustomException;
import com.openmpy.wiki.global.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final Snowflake snowflake = new Snowflake();
    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;

    @Transactional
    public DocumentCreateResponse createDocument(final DocumentCreateRequest request, final String clientIp) {
        final DocumentCategory category = DocumentCategory.from(request.category());

        if (documentRepository.existsByTitleAndCategory(request.title(), category)) {
            throw new CustomException("해당 카테고리에 이미 작성된 문서입니다.");
        }

        final Document document = Document.create(snowflake.nextId(), request.title(), category);
        final DocumentHistory documentHistory = DocumentHistory.create(
                snowflake.nextId(), request.author(), request.content(), clientIp, document
        );
        document.addHistory(documentHistory);

        final Document savedDocument = documentRepository.save(document);
        return new DocumentCreateResponse(savedDocument.getId());
    }

    @Transactional
    public DocumentUpdateResponse updateDocument(
            final Long documentId, final DocumentUpdateRequest request, final String clientIp
    ) {
        final Document document = getDocument(documentId);
        final DocumentHistory documentHistory = DocumentHistory.create(
                snowflake.nextId(), request.author(), request.content(), clientIp, document
        );
        document.addHistory(documentHistory);
        return new DocumentUpdateResponse(documentId);
    }

    @Transactional
    public void deleteDocument(
            final Long documentId
    ) {
        final Document document = getDocument(documentId);
        documentRepository.delete(document);
    }

    @Transactional
    public void deleteDocumentHistory(
            final Long documentHistoryId
    ) {
        final DocumentHistory documentHistory = documentHistoryRepository.findById(documentHistoryId).orElseThrow(
                () -> new CustomException("찾을 수 없는 문서 기록 번호입니다.")
        );
        if (documentHistory.isDeleted()) {
            throw new CustomException("이미 삭제된 문서 기록입니다.");
        }

        documentHistory.delete();
    }

    @Transactional(readOnly = true)
    public DocumentReadResponse readLatestDocument(final Long documentId) {
        final Document document = getDocument(documentId);
        final DocumentHistory documentHistory = documentHistoryRepository
                .findFirstByDocumentAndDeletedFalseOrderByVersionDesc(document)
                .orElseThrow(
                        () -> new CustomException("문서 기록이 존재하지 않습니다.")
                );

        return DocumentReadResponse.from(document, documentHistory);
    }

    private Document getDocument(final Long documentId) {
        return documentRepository.findById(documentId).orElseThrow(
                () -> new CustomException("찾을 수 없는 문서 번호입니다.")
        );
    }
}
