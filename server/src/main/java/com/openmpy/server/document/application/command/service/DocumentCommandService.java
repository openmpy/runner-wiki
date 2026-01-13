package com.openmpy.server.document.application.command.service;

import com.openmpy.server.document.application.command.dto.request.DocumentCreateRequest;
import com.openmpy.server.document.application.command.dto.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.command.dto.response.DocumentCreateResponse;
import com.openmpy.server.document.application.command.dto.response.DocumentUpdateResponse;
import com.openmpy.server.document.application.command.usecase.CreateDocumentUseCase;
import com.openmpy.server.document.application.command.usecase.DeleteDocumentHistoryUseCase;
import com.openmpy.server.document.application.command.usecase.DeleteDocumentUseCase;
import com.openmpy.server.document.application.command.usecase.UpdateDocumentUseCase;
import com.openmpy.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentCommandService {

    private final CreateDocumentUseCase createDocumentUseCase;
    private final UpdateDocumentUseCase updateDocumentUseCase;
    private final DeleteDocumentUseCase deleteDocumentUseCase;
    private final DeleteDocumentHistoryUseCase deleteDocumentHistoryUseCase;

    @Transactional
    public DocumentCreateResponse create(final DocumentCreateRequest request, final String clientIp) {
        try {
            return createDocumentUseCase.execute(request, clientIp);
        } catch (final DataIntegrityViolationException e) {
            throw new CustomException("문서가 중복으로 작성되었습니다.");
        }
    }

    @Transactional
    public DocumentUpdateResponse update(
            final Long documentId,
            final DocumentUpdateRequest request,
            final String clientIp
    ) {
        try {
            return updateDocumentUseCase.execute(documentId, request, clientIp);
        } catch (final DataIntegrityViolationException e) {
            throw new CustomException("문서 기록이 중복으로 작성되었습니다.");
        }
    }

    @Transactional
    public void delete(final Long documentId) {
        deleteDocumentUseCase.execute(documentId);
    }

    @Transactional
    public void deleteHistory(final Long documentHistoryId) {
        deleteDocumentHistoryUseCase.execute(documentHistoryId);
    }
}
