package com.openmpy.wiki.document.application;

import com.openmpy.wiki.admin.application.response.AdminDocumentHistoryReadResponse;
import com.openmpy.wiki.document.application.request.DocumentCreateRequest;
import com.openmpy.wiki.document.application.request.DocumentUpdateRequest;
import com.openmpy.wiki.document.application.response.DocumentCreateResponse;
import com.openmpy.wiki.document.application.response.DocumentHistoryReadResponses;
import com.openmpy.wiki.document.application.response.DocumentReadResponse;
import com.openmpy.wiki.document.application.response.DocumentReadResponses;
import com.openmpy.wiki.document.application.response.DocumentUpdateResponse;
import com.openmpy.wiki.document.domain.constants.DocumentCategory;
import com.openmpy.wiki.document.domain.constants.DocumentStatus;
import com.openmpy.wiki.document.domain.entity.Document;
import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import com.openmpy.wiki.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.wiki.document.domain.repository.DocumentRepository;
import com.openmpy.wiki.global.dto.PageResponse;
import com.openmpy.wiki.global.exception.CustomException;
import com.openmpy.wiki.global.snowflake.Snowflake;
import com.openmpy.wiki.global.utils.PageLimitCalculator;
import com.openmpy.wiki.view.application.ViewService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final Snowflake snowflake = new Snowflake();
    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;
    private final ViewService viewService;

    @Transactional
    public DocumentCreateResponse createDocument(final DocumentCreateRequest request, final String clientIp) {
        final DocumentCategory category = DocumentCategory.of(request.category());

        if (documentRepository.existsByTitle_ValueAndCategory(request.title(), category)) {
            throw new CustomException("해당 카테고리에 이미 작성된 문서입니다.");
        }

        final Document document = Document.create(snowflake.nextId(), request.title(), category);
        final DocumentHistory documentHistory = DocumentHistory.create(
                snowflake.nextId(), request.author(), request.content(), clientIp, 1, document
        );
        document.addHistory(documentHistory);
        documentRepository.save(document);

        return new DocumentCreateResponse(document.getId());
    }

    @Transactional
    public DocumentUpdateResponse updateDocument(
            final String documentId, final DocumentUpdateRequest request, final String clientIp
    ) {
        final Document document = getDocument(documentId);
        final long version = documentHistoryRepository.getLatestVersionByDocumentId(document) + 1;

        final DocumentHistory documentHistory = DocumentHistory.create(
                snowflake.nextId(), request.author(), request.content(), clientIp, version, document
        );
        document.addHistory(documentHistory);

        return new DocumentUpdateResponse(documentId);
    }

    @Transactional
    public void updateDocumentStatus(final String documentId, final String status) {
        final Document document = getDocument(documentId);
        final DocumentStatus documentStatus = DocumentStatus.from(status);
        document.updateStatus(documentStatus);
    }

    @Transactional
    public void deleteDocument(
            final String documentId
    ) {
        final Document document = getDocumentWithHistory(documentId);
        documentRepository.delete(document);
    }

    @Transactional
    public void deleteDocumentHistory(
            final String documentHistoryId
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
    public DocumentReadResponse readLatestDocument(final String documentId) {
        final DocumentHistory documentHistory = documentHistoryRepository
                .findLatestHistoryWithDocument(documentId, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElseThrow(() -> new CustomException("문서 기록이 존재하지 않습니다."));

        return DocumentReadResponse.from(documentHistory.getDocument(), documentHistory);
    }

    @Transactional(readOnly = true)
    public DocumentReadResponse readDocumentHistory(final String documentHistoryId) {
        final DocumentHistory documentHistory = documentHistoryRepository.findByIdWithDocument(documentHistoryId)
                .orElseThrow(() -> new CustomException("찾을 수 없는 문서 기록 번호입니다."));

        return DocumentReadResponse.from(documentHistory.getDocument(), documentHistory);
    }

    @Transactional(readOnly = true)
    public PageResponse<List<DocumentReadResponse>> readLatestDocuments(
            final int page, final int size
    ) {
        final List<DocumentReadResponse> responses = documentRepository
                .findAllOrderByUpdatedAtDesc((page - 1) * size, size).stream()
                .map(DocumentReadResponse::from)
                .toList();

        return new PageResponse<>(
                responses,
                page,
                size,
                documentRepository.count(PageLimitCalculator.calculatePageLimit(page, size, 10))
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<List<DocumentReadResponse>> readDocuments(
            final int page, final int size
    ) {
        final List<DocumentReadResponse> responses = documentRepository
                .findAllOrderByCreatedAtDesc((page - 1) * size, size).stream()
                .map(DocumentReadResponse::from)
                .toList();

        return new PageResponse<>(
                responses,
                page,
                size,
                documentRepository.count(PageLimitCalculator.calculatePageLimit(page, size, 10))
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<DocumentHistoryReadResponses> readDocumentHistories(
            final String documentId, final int page, final int size
    ) {
        final Document document = getDocument(documentId);
        final List<DocumentHistory> documentHistories = documentHistoryRepository.findAllByDocumentAndDeletedFalse(
                documentId, (page - 1) * size, size
        );
        final DocumentHistoryReadResponses responses = DocumentHistoryReadResponses.from(document, documentHistories);

        return new PageResponse<>(
                responses,
                page,
                size,
                documentHistoryRepository.countByDocumentAndDeletedFalse(
                        documentId, PageLimitCalculator.calculatePageLimit(page, size, 10)
                )
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<List<AdminDocumentHistoryReadResponse>> readDocumentHistories(
            final int page, final int size
    ) {
        final List<AdminDocumentHistoryReadResponse> responses = documentHistoryRepository.findAll(
                        (page - 1) * size, size
                )
                .stream()
                .map(AdminDocumentHistoryReadResponse::from)
                .toList();

        return new PageResponse<>(
                responses,
                page,
                size,
                documentHistoryRepository.count(PageLimitCalculator.calculatePageLimit(page, size, 10))
        );
    }

    @Transactional(readOnly = true)
    public DocumentReadResponses readPopularDocuments() {
        final List<String> viewScore = viewService.getViewScore();

        final Map<String, Document> documentMap = documentRepository.findByIdIn(viewScore)
                .stream()
                .collect(Collectors.toMap(Document::getId, Function.identity()));

        final List<DocumentReadResponse> responses = viewScore.stream()
                .map(documentMap::get)
                .filter(Objects::nonNull) // 존재하지 않는 ID 필터링
                .map(DocumentReadResponse::from)
                .toList();

        return new DocumentReadResponses(responses);
    }

    @Transactional(readOnly = true)
    public DocumentReadResponse readRandomDocument() {
        final Document document = documentRepository.findRandomDocument().orElseThrow(
                () -> new CustomException("문서가 존재하지 않습니다.")
        );
        return DocumentReadResponse.from(document);
    }

    private Document getDocument(final String documentId) {
        return documentRepository.findById(documentId).orElseThrow(
                () -> new CustomException("찾을 수 없는 문서 번호입니다.")
        );
    }

    private Document getDocumentWithHistory(final String documentId) {
        return documentRepository.findByIdWithHistory(documentId).orElseThrow(
                () -> new CustomException("찾을 수 없는 문서 번호입니다.")
        );
    }
}
