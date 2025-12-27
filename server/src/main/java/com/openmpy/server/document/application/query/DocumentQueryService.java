package com.openmpy.server.document.application.query;

import com.openmpy.server.document.application.query.response.DocumentGetResponse;
import com.openmpy.server.document.application.query.response.DocumentHistoryPageResponse;
import com.openmpy.server.document.application.query.response.DocumentPageResponse;
import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.model.DocumentHistory;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.global.dto.PageResponse;
import com.openmpy.server.global.exception.CustomException;
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
    private final DocumentQueryRepository documentQueryRepository;

    @Transactional(readOnly = true)
    public DocumentGetResponse getHistory(final Long documentHistoryId) {
        final DocumentHistory documentHistory = documentHistoryRepository.findById(documentHistoryId).orElseThrow(
                () -> new CustomException("찾을 수 없는 문서 기록 번호입니다.")
        );

        return DocumentGetResponse.from(documentHistory.getDocument(), documentHistory);
    }

    @Transactional(readOnly = true)
    public PageResponse<DocumentHistoryPageResponse> getHistories(
            final Long documentId,
            final int page,
            final int size
    ) {
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Page<DocumentHistory> documentHistoryPage = documentHistoryRepository.findAllByDocumentId(
                documentId,
                pageRequest
        );
        final List<DocumentHistoryPageResponse> documentHistoryResponses = documentHistoryPage.getContent().stream()
                .map(DocumentHistoryPageResponse::from)
                .toList();

        return PageResponse.of(documentHistoryResponses, page, size, documentHistoryPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public DocumentGetResponse getLatest(final Long documentId) {
        final DocumentGetResponse response = documentQueryRepository.findLatestDocumentById(documentId);

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
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

        if (category.equalsIgnoreCase(DOCUMENT_CATEGORY_ALL)) {
            final Page<Document> documentPage = documentRepository.findAll(pageRequest);

            return convertToDocumentPageResponse(documentPage);
        }

        final DocumentCategory selectedCategory = DocumentCategory.valueOf(category.toUpperCase());
        final Page<Document> documentPage = documentRepository.findAllByCategory(selectedCategory, pageRequest);

        return convertToDocumentPageResponse(documentPage);
    }

    private PageResponse<DocumentPageResponse> convertToDocumentPageResponse(final Page<Document> documentPage) {
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
