package com.openmpy.server.document.application;

import com.openmpy.server.document.application.request.DocumentCreateRequest;
import com.openmpy.server.document.application.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.response.DocumentCreateResponse;
import com.openmpy.server.document.application.response.DocumentGetResponse;
import com.openmpy.server.document.application.response.DocumentUpdateResponse;
import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.global.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Transactional
    public DocumentCreateResponse create(final DocumentCreateRequest request, final HttpServletRequest servletRequest) {
        if (documentRepository.existsByTitleAndCategory(request.title(), request.category())) {
            throw new IllegalArgumentException("이미 작성된 문서입니다.");
        }

        final Document document = Document.create(
                request.title(),
                request.category(),
                request.author(),
                request.content(),
                getContentByteSize(request.content()),
                ClientIpUtil.getClientIp(servletRequest)
        );
        final Document savedDocument = documentRepository.save(document);

        return new DocumentCreateResponse(savedDocument.getId());
    }

    @Transactional
    public DocumentUpdateResponse update(
            final Long documentId,
            final DocumentUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final Document document = findDocumentById(documentId);
        final Long documentVersion = document.getMaximumVersion() + 1;

        document.addHistory(
                request.author(),
                request.content(),
                documentVersion,
                getContentByteSize(request.content()),
                ClientIpUtil.getClientIp(servletRequest)
        );
        return new DocumentUpdateResponse(document.getId());
    }

    @Transactional(readOnly = true)
    public DocumentGetResponse getLatest(final Long documentId) {
        final Document document = findDocumentById(documentId);

        return DocumentGetResponse.of(document);
    }

    private Document findDocumentById(final Long documentId) {
        return documentRepository.findById(documentId).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 문서 번호입니다.")
        );
    }

    private long getContentByteSize(final String content) {
        return content.getBytes(StandardCharsets.UTF_8).length;
    }
}
