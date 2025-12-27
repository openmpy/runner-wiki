package com.openmpy.server.document.application.usecase;

import com.openmpy.server.document.application.command.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.command.response.DocumentUpdateResponse;
import com.openmpy.server.document.application.support.ContentSizeCalculator;
import com.openmpy.server.document.application.support.ImageAttacher;
import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.global.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateDocumentUseCase {

    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;

    private final ImageAttacher imageAttacher;
    private final ContentSizeCalculator contentSizeCalculator;

    @Transactional
    public DocumentUpdateResponse execute(
            final Long documentId,
            final DocumentUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final Document document = findDocumentById(documentId);
        final String clientIp = ClientIpUtil.getClientIp(servletRequest);

        final long nextVersion = documentHistoryRepository.findMaxVersion(document.getId()) + 1;

        document.addHistory(
                request.author(),
                request.content(),
                nextVersion,
                contentSizeCalculator.calculateUtf8Bytes(request.content()),
                clientIp
        );
        imageAttacher.attachTempImages(document, request.imageIds());
        return new DocumentUpdateResponse(document.getId());
    }

    private Document findDocumentById(final Long documentId) {
        return documentRepository.findById(documentId).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 문서 번호입니다.")
        );
    }
}
