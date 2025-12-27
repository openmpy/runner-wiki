package com.openmpy.server.document.application.usecase;

import com.openmpy.server.document.application.command.request.DocumentCreateRequest;
import com.openmpy.server.document.application.command.response.DocumentCreateResponse;
import com.openmpy.server.document.application.support.ContentSizeCalculator;
import com.openmpy.server.document.application.support.ImageAttacher;
import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.global.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateDocumentUseCase {

    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;

    private final ImageAttacher imageAttacher;
    private final ContentSizeCalculator contentSizeCalculator;

    @Transactional
    public DocumentCreateResponse execute(
            final DocumentCreateRequest request,
            final HttpServletRequest servletRequest
    ) {
        validateDuplicate(request);

        final String clientIp = ClientIpUtil.getClientIp(servletRequest);

        final Document document = Document.create(request.title(), request.category());
        final Document savedDocument = documentRepository.save(document);

        final long nextVersion = documentHistoryRepository.findMaxVersion(savedDocument.getId()) + 1;

        savedDocument.addHistory(
                request.author(),
                request.content(),
                nextVersion,
                contentSizeCalculator.calculateUtf8Bytes(request.content()),
                clientIp
        );
        imageAttacher.attachTempImages(savedDocument, request.imageIds());
        return new DocumentCreateResponse(savedDocument.getId());
    }

    private void validateDuplicate(final DocumentCreateRequest request) {
        if (documentRepository.existsByTitleAndCategory(request.title(), request.category())) {
            throw new IllegalArgumentException("이미 작성된 문서입니다.");
        }
    }
}
