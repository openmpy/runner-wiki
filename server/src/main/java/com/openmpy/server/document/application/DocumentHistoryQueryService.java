package com.openmpy.server.document.application;

import com.openmpy.server.document.domain.entity.DocumentHistory;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.dto.response.DocumentGetResponse;
import com.openmpy.server.document.dto.response.DocumentHistoryPageResponse;
import com.openmpy.server.global.dto.PageResponse;
import com.openmpy.server.global.exception.CustomException;
import com.openmpy.server.global.util.PageLimitCalculator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentHistoryQueryService {

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
}
