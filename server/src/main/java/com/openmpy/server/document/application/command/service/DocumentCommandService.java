package com.openmpy.server.document.application.command.service;

import com.openmpy.server.document.application.command.usecase.DeleteDocumentHistoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentCommandService {

    private final DeleteDocumentHistoryUseCase deleteDocumentHistoryUseCase;

    @Transactional
    public void deleteHistory(final Long documentHistoryId) {
        deleteDocumentHistoryUseCase.execute(documentHistoryId);
    }
}
