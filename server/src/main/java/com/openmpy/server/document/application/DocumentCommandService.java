package com.openmpy.server.document.application;

import com.openmpy.server.document.application.support.ImageAttacher;
import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.dto.request.DocumentCreateRequest;
import com.openmpy.server.document.dto.request.DocumentUpdateRequest;
import com.openmpy.server.document.dto.response.DocumentCreateResponse;
import com.openmpy.server.document.dto.response.DocumentUpdateResponse;
import com.openmpy.server.global.exception.CustomException;
import com.openmpy.server.global.util.ContentCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentCommandService {

    private final DocumentRepository documentRepository;
    private final ImageAttacher imageAttacher;

    @Transactional
    public DocumentCreateResponse save(
        final DocumentCreateRequest request,
        final String clientIp
    ) {
        validateDuplicate(request);

        final Document document = Document.create(request.title(), request.category());
        final Document savedDocument = documentRepository.save(document);

        savedDocument.addHistory(
            request.author(),
            request.content(),
            ContentCalculator.calculateUtf8Bytes(request.content()),
            clientIp
        );
        imageAttacher.attachTempImages(savedDocument, request.imageIds());
        return new DocumentCreateResponse(savedDocument.getId());
    }

    @Transactional
    public DocumentUpdateResponse update(
        final Long documentId,
        final DocumentUpdateRequest request,
        final String clientIp
    ) {
        final Document document = findDocumentById(documentId);

        document.addHistory(
            request.author(),
            request.content(),
            ContentCalculator.calculateUtf8Bytes(request.content()),
            clientIp
        );
        imageAttacher.attachTempImages(document, request.imageIds());
        return new DocumentUpdateResponse(document.getId());
    }

    @Transactional
    public void delete(final Long documentId) {
        final Document document = findDocumentById(documentId);

        document.delete();
    }

    private void validateDuplicate(final DocumentCreateRequest request) {
        if (documentRepository.existsByTitle_ValueAndCategory(
            request.title(),
            request.category())
        ) {
            throw new CustomException("이미 작성된 문서입니다.");
        }
    }

    private Document findDocumentById(final Long documentId) {
        return documentRepository.findById(documentId)
            .orElseThrow(() -> new CustomException("찾을 수 없는 문서 번호입니다."));
    }
}
