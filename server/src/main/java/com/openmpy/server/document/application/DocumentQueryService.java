package com.openmpy.server.document.application;

import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.entity.DocumentHistory;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.document.dto.response.DocumentGetResponse;
import com.openmpy.server.document.dto.response.DocumentHistoryPageResponse;
import com.openmpy.server.document.dto.response.DocumentPageResponse;
import com.openmpy.server.global.dto.CursorResponse;
import com.openmpy.server.global.dto.PageResponse;
import com.openmpy.server.global.dto.SliceResponse;
import com.openmpy.server.global.exception.CustomException;
import com.openmpy.server.global.util.PageLimitCalculator;
import com.openmpy.server.global.util.SearchInputClassifier;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentQueryService {

    private static final String DOCUMENT_CATEGORY_ALL = "all";
    public static final int MOVABLE_PAGE_COUNT = 10;
    public static final int MAX_PAGE_LIMIT = 10000;

    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;

    @Transactional(readOnly = true)
    public DocumentGetResponse getHistory(final Long documentHistoryId) {
        final DocumentHistory documentHistory = documentHistoryRepository.findByIdWithDocument(
                documentHistoryId
            )
            .orElseThrow(() -> new CustomException("찾을 수 없는 문서 기록 번호입니다."));

        return DocumentGetResponse.from(documentHistory.getDocument(), documentHistory);
    }

    @Transactional(readOnly = true)
    public PageResponse<DocumentHistoryPageResponse> getHistories(
        final Long documentId,
        final int page,
        final int size
    ) {
        final int offset = page * size;

        final List<DocumentHistoryPageResponse> responses = documentHistoryRepository.findAllByDocumentId(
                documentId, offset, size
            )
            .stream()
            .map(DocumentHistoryPageResponse::from)
            .toList();
        final Long totalElements = documentHistoryRepository.countByDocumentId(
            documentId,
            PageLimitCalculator.calculatePageLimit(page, size, size)
        );

        return PageResponse.of(responses, page, size, totalElements);
    }

    @Transactional(readOnly = true)
    public DocumentGetResponse getLatest(final Long documentId) {
        final DocumentGetResponse response = documentRepository.findLatestDocumentById(
            documentId
        );

        if (response == null) {
            throw new CustomException("문서 또는 문서 기록이 존재하지 않습니다.");
        }
        return response;
    }

    @Transactional(readOnly = true)
    public PageResponse<DocumentPageResponse> getLatestDocumentsV1(
        final String category,
        final int page,
        final int size
    ) {
        validatePage(page);

        final Sort sort = Sort.by(Direction.DESC, "updatedAt").descending();
        final PageRequest pageRequest = PageRequest.of(page, size, sort);

        if (category.equalsIgnoreCase(DOCUMENT_CATEGORY_ALL)) {
            final Page<Document> documentPage = documentRepository.findAll(pageRequest);
            final List<DocumentPageResponse> responses = documentPage
                .stream()
                .map(DocumentPageResponse::from)
                .toList();

            return PageResponse.of(responses, page, size, documentPage.getTotalElements());
        }

        final Page<Document> documentPage = documentRepository.findPageByCategory(
            DocumentCategory.valueOf(category), pageRequest
        );
        final List<DocumentPageResponse> responses = documentPage
            .stream()
            .map(DocumentPageResponse::from)
            .toList();

        return PageResponse.of(responses, page, size, documentPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PageResponse<DocumentPageResponse> getLatestDocumentsV2(
        final String category,
        final int page,
        final int size
    ) {
        validatePage(page);

        final int offset = page * size;

        if (category.equalsIgnoreCase(DOCUMENT_CATEGORY_ALL)) {
            final List<DocumentPageResponse> responses = documentRepository.findAllOrderByUpdatedAtDesc(
                    offset, size
                )
                .stream()
                .map(DocumentPageResponse::from)
                .toList();
            final Long totalElements = documentRepository.count(
                PageLimitCalculator.calculatePageLimit(page, size, MOVABLE_PAGE_COUNT)
            );

            return PageResponse.of(responses, page, size, totalElements);
        }

        final List<DocumentPageResponse> responses = documentRepository.findAllByCategoryOrderByUpdatedAtDesc(
                category.toUpperCase(), offset, size
            )
            .stream()
            .map(DocumentPageResponse::from)
            .toList();
        final Long totalElements = documentRepository.countByCategory(
            category.toUpperCase(),
            PageLimitCalculator.calculatePageLimit(page, size, MOVABLE_PAGE_COUNT)
        );

        return PageResponse.of(responses, page, size, totalElements);
    }

    @Transactional(readOnly = true)
    public SliceResponse<DocumentPageResponse> searchDocumentsV1(
        final String keyword,
        final int page,
        final int size
    ) {
        final Sort sort = Sort.by(Direction.DESC, "updatedAt").descending();
        final PageRequest pageRequest = PageRequest.of(page, size, sort);

        if (SearchInputClassifier.isChosungQuery(keyword)) {
            final Slice<Document> documentSlice = documentRepository.findAllByTitleChosung_ValueStartingWith(
                keyword,
                pageRequest
            );

            return convertSearchSliceResponse(page, size, documentSlice);
        }

        final Slice<Document> documentSlice = documentRepository.findAllByTitle_ValueStartingWith(
            keyword,
            pageRequest
        );
        return convertSearchSliceResponse(page, size, documentSlice);
    }

    @Transactional(readOnly = true)
    public CursorResponse<DocumentPageResponse> searchDocumentsV2(
        final String keyword,
        final Long cursorId,
        final int size
    ) {
        final int fetchSize = size + 1;

        if (SearchInputClassifier.isChosungQuery(keyword)) {
            final List<Document> documents = documentRepository.findAllByTitleChosungStartingWithCursor(
                keyword,
                cursorId,
                fetchSize
            );

            return convertSearchCursorResponse(size, documents);
        }

        final List<Document> documents = documentRepository.findAllByTitleStartingWithCursor(
            keyword,
            cursorId,
            fetchSize
        );

        return convertSearchCursorResponse(size, documents);
    }

    @Transactional(readOnly = true)
    public DocumentPageResponse getShuffleDocument() {
        final Document document = documentRepository.findRandomDocument();
        return DocumentPageResponse.from(document);
    }

    private void validatePage(final int page) {
        if (page > MAX_PAGE_LIMIT) {
            throw new CustomException("최대 10,000 페이지까지만 조회할 수 있습니다.");
        }
    }

    private SliceResponse<DocumentPageResponse> convertSearchSliceResponse(
        final int page,
        final int size,
        final Slice<Document> documentSlice
    ) {
        final List<DocumentPageResponse> responses = documentSlice.stream()
            .map(DocumentPageResponse::from)
            .toList();

        return new SliceResponse<>(
            responses,
            page,
            size,
            documentSlice.hasNext(),
            documentSlice.hasPrevious()
        );
    }

    private CursorResponse<DocumentPageResponse> convertSearchCursorResponse(
        final int size,
        final List<Document> documents
    ) {
        final List<DocumentPageResponse> responses = documents
            .stream()
            .map(DocumentPageResponse::from)
            .toList();
        final boolean hasNext = documents.size() > size;

        return new CursorResponse<>(
            responses,
            hasNext ? documents.get(size).getId() : null,
            hasNext
        );
    }
}
