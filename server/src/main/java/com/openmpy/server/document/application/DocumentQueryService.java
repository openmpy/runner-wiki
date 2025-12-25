package com.openmpy.server.document.application;

import com.openmpy.server.document.application.response.DocumentGetResponse;
import com.openmpy.server.document.domain.entity.DocumentHistory;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentQueryService {

    private final DocumentHistoryRepository documentHistoryRepository;

    @Transactional(readOnly = true)
    public DocumentGetResponse getHistory(final Long documentHistoryId) {
        final DocumentHistory documentHistory = documentHistoryRepository.findById(documentHistoryId).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 문서 기록 번호입니다.")
        );

        return DocumentGetResponse.from(documentHistory.getDocument(), documentHistory);
    }
}
