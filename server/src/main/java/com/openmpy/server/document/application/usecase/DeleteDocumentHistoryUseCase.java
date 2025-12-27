package com.openmpy.server.document.application.usecase;

import com.openmpy.server.document.domain.model.DocumentHistory;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteDocumentHistoryUseCase {

    private final DocumentHistoryRepository documentHistoryRepository;

    @Transactional
    public void execute(final Long documentHistoryId) {
        final DocumentHistory documentHistory = documentHistoryRepository.findById(documentHistoryId).orElseThrow(
                () -> new CustomException("찾을 수 없는 문서 기록 번호입니다.")
        );

        documentHistory.delete();
    }
}
