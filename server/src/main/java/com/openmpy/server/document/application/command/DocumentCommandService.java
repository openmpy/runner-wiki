package com.openmpy.server.document.application.command;

import com.openmpy.server.document.application.command.request.DocumentCreateRequest;
import com.openmpy.server.document.application.command.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.command.response.DocumentCreateResponse;
import com.openmpy.server.document.application.command.response.DocumentUpdateResponse;
import com.openmpy.server.document.application.usecase.CreateDocumentUseCase;
import com.openmpy.server.document.application.usecase.DeleteDocumentHistoryUseCase;
import com.openmpy.server.document.application.usecase.DeleteDocumentUseCase;
import com.openmpy.server.document.application.usecase.UpdateDocumentUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    public DocumentCreateResponse create(final DocumentCreateRequest request, final HttpServletRequest servletRequest) {
        return createDocumentUseCase.execute(request, servletRequest);
    }

    @Transactional
    public DocumentUpdateResponse update(
            final Long documentId,
            final DocumentUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        return updateDocumentUseCase.execute(documentId, request, servletRequest);
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
