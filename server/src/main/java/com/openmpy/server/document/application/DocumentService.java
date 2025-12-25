package com.openmpy.server.document.application;

import com.openmpy.server.document.application.request.DocumentCreateRequest;
import com.openmpy.server.document.application.response.DocumentCreateResponse;
import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Transactional
    public DocumentCreateResponse create(final DocumentCreateRequest request) {
        if (documentRepository.existsByTitleAndCategory(request.title(), request.category())) {
            throw new IllegalArgumentException("이미 작성된 문서입니다.");
        }

        final Document document = Document.create(
                request.title(),
                request.category(),
                request.author(),
                request.content(),
                0L,
                ""
        );
        final Document savedDocument = documentRepository.save(document);

        return new DocumentCreateResponse(savedDocument.getId());
    }
}
