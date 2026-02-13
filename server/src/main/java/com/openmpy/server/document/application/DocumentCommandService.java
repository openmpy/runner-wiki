package com.openmpy.server.document.application;

import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.document.dto.request.DocumentCreateRequest;
import com.openmpy.server.document.dto.request.DocumentUpdateRequest;
import com.openmpy.server.document.dto.response.DocumentCreateResponse;
import com.openmpy.server.document.dto.response.DocumentUpdateResponse;
import com.openmpy.server.global.exception.CustomException;
import com.openmpy.server.global.util.ContentCalculator;
import com.openmpy.server.image.application.port.ImageStoragePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentCommandService {

    private final DocumentRepository documentRepository;
    private final ImageStoragePort imageStoragePort;

    @Transactional
    public DocumentCreateResponse save(
        final DocumentCreateRequest request,
        final String clientIp
    ) {
        try {
            validateDuplicate(request);

            final Document document = Document.create(request.title(), request.category());

            document.addHistory(
                request.author(),
                imageStoragePort.convertTempToImageUrl(request.content()),
                ContentCalculator.calculateUtf8Bytes(request.content()),
                clientIp
            );
            documentRepository.save(document);

            if (request.imageUrls() != null) {
                useImages(request.imageUrls());
            }
            return new DocumentCreateResponse(document.getId());
        } catch (final DataIntegrityViolationException e) {
            throw new CustomException("이미 작성된 문서입니다.");
        }
    }

    @Transactional
    public DocumentUpdateResponse update(
        final Long documentId,
        final DocumentUpdateRequest request,
        final String clientIp
    ) {
        try {
            final Document document = documentRepository.findByIdForUpdate(documentId)
                .orElseThrow(() -> new CustomException("찾을 수 없는 문서 번호입니다."));

            document.addHistory(
                request.author(),
                imageStoragePort.convertTempToImageUrl(request.content()),
                ContentCalculator.calculateUtf8Bytes(request.content()),
                clientIp
            );

            if (request.imageUrls() != null) {
                useImages(request.imageUrls());
            }
            return new DocumentUpdateResponse(document.getId());
        } catch (final DataIntegrityViolationException e) {
            throw new CustomException("동시 처리 중 충돌이 발생했습니다.");
        }
    }

    @Transactional
    public void delete(final Long documentId) {
        final Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new CustomException("찾을 수 없는 문서 번호입니다."));

        document.delete();
    }

    private void validateDuplicate(final DocumentCreateRequest request) {
        if (documentRepository.existsByTitle_ValueAndCategory(
            request.title(),
            request.category()
        )) {
            throw new CustomException("이미 작성된 문서입니다.");
        }
    }

    private void useImages(final List<String> imageUrls) {
        for (final String imageUrl : imageUrls) {
            imageStoragePort.useImage(imageUrl);
        }
    }
}
