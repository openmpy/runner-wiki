package com.openmpy.server.document.application;

import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.entity.DocumentHistory;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.dto.response.DocumentGetResponse;
import com.openmpy.server.document.dto.response.DocumentHistoryPageResponse;
import com.openmpy.server.document.dto.response.DocumentPageResponse;
import com.openmpy.server.global.dto.PageResponse;
import com.openmpy.server.global.exception.CustomException;
import com.openmpy.server.global.util.PageLimitCalculator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentQueryService {

    private static final String DOCUMENT_CATEGORY_ALL = "all";

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
    public PageResponse<DocumentPageResponse> getLatestDocuments(
        final String category,
        final int page,
        final int size
    ) {
        final int offset = page * size;

        if (category.equalsIgnoreCase(DOCUMENT_CATEGORY_ALL)) {
            final List<DocumentPageResponse> responses = documentRepository.findAllOrderByUpdatedAtDesc(
                    offset, size
                )
                .stream()
                .map(DocumentPageResponse::from)
                .toList();
            final Long totalElements = documentRepository.count(
                PageLimitCalculator.calculatePageLimit(page, size, size)
            );

            return PageResponse.of(responses, page, size, totalElements);
        }

        final List<DocumentPageResponse> responses = documentRepository.findAllByCategoryOrderByUpdatedAtDesc(
                category.toUpperCase(), offset, size
            ).stream()
            .map(DocumentPageResponse::from)
            .toList();
        final Long totalElements = documentRepository.countByCategory(
            category.toUpperCase(), PageLimitCalculator.calculatePageLimit(page, size, size)
        );

        return PageResponse.of(responses, page, size, totalElements);
    }

    @Transactional(readOnly = true)
    public PageResponse<DocumentPageResponse> searchDocuments(
        final String title,
        final int page,
        final int size
    ) {
        final PageRequest pageRequest = PageRequest.of(page, size,
            Sort.by(Sort.Direction.DESC, "updatedAt"));
        final Page<Document> documentPage = documentRepository.findAllByTitle_ValueContainingIgnoreCase(
            title, pageRequest
        );

        return convertToDocumentPageResponse(documentPage);
    }

    @Transactional(readOnly = true)
    public DocumentPageResponse getShuffleDocument() {
        final Document document = documentRepository.findRandomDocument();
        return DocumentPageResponse.from(document);
    }

    private PageResponse<DocumentPageResponse> convertToDocumentPageResponse(
        final Page<Document> documentPage) {
        final List<DocumentPageResponse> documentResponses = documentPage.getContent().stream()
            .map(DocumentPageResponse::from)
            .toList();

        return PageResponse.of(
            documentResponses,
            documentPage.getNumber(),
            documentPage.getSize(),
            documentPage.getTotalElements()
        );
    }
}
