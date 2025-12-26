package com.openmpy.server.document.application.command;

import com.openmpy.server.document.application.command.request.DocumentCreateRequest;
import com.openmpy.server.document.application.command.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.command.response.DocumentCreateResponse;
import com.openmpy.server.document.application.command.response.DocumentUpdateResponse;
import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.model.DocumentHistory;
import com.openmpy.server.document.domain.model.DocumentImage;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.global.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentCommandService {

    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;
    private final DocumentImageRepository documentImageRepository;

    @Transactional
    public DocumentCreateResponse create(final DocumentCreateRequest request, final HttpServletRequest servletRequest) {
        validateDuplicate(request);

        final String clientIp = ClientIpUtil.getClientIp(servletRequest);
        final Document document = Document.create(request.title(), request.category());
        final Document savedDocument = documentRepository.save(document);
        final long nextVersion = documentHistoryRepository.findMaxVersion(savedDocument.getId()) + 1;

        savedDocument.addHistory(
                request.author(),
                request.content(),
                nextVersion,
                getContentByteSize(request.content()),
                clientIp
        );
        attachImages(savedDocument, request.imageIds());
        return new DocumentCreateResponse(savedDocument.getId());
    }

    @Transactional
    public DocumentUpdateResponse update(
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
                getContentByteSize(request.content()),
                clientIp
        );
        attachImages(document, request.imageIds());
        return new DocumentUpdateResponse(document.getId());
    }

    @Transactional
    public void delete(final Long documentId) {
        final Document document = findDocumentById(documentId);

        document.delete();
    }

    @Transactional
    public void deleteHistory(final Long documentHistoryId) {
        final DocumentHistory documentHistory = documentHistoryRepository.findById(documentHistoryId).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 문서 기록 번호입니다.")
        );

        documentHistory.delete();
    }

    private void validateDuplicate(final DocumentCreateRequest request) {
        if (documentRepository.existsByTitleAndCategory(request.title(), request.category())) {
            throw new IllegalArgumentException("이미 작성된 문서입니다.");
        }
    }

    private Document findDocumentById(final Long documentId) {
        return documentRepository.findById(documentId).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 문서 번호입니다.")
        );
    }

    private long getContentByteSize(final String content) {
        return content.getBytes(StandardCharsets.UTF_8).length;
    }

    private void attachImages(final Document document, final List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) {
            return;
        }

        final List<DocumentImage> images = documentImageRepository.findAllById(imageIds);

        document.attachImages(images);
    }
}
