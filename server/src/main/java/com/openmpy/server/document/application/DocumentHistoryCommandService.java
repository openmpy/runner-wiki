package com.openmpy.server.document.application;

import com.openmpy.server.document.domain.entity.DocumentHistory;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentHistoryCommandService {

    private final DocumentHistoryRepository documentHistoryRepository;

    @Transactional
    public void delete(final Long documentHistoryId) {
        final DocumentHistory documentHistory = documentHistoryRepository.findById(
                documentHistoryId
            )
            .orElseThrow(() -> new CustomException("찾을 수 없는 문서 기록 번호입니다."));

        documentHistory.delete();
    }
}
