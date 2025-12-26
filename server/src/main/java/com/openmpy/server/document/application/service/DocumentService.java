package com.openmpy.server.document.application.service;

import com.openmpy.server.document.application.port.ImageStorage;
import com.openmpy.server.document.application.port.UploadedImage;
import com.openmpy.server.document.application.request.DocumentCreateRequest;
import com.openmpy.server.document.application.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.response.DocumentCreateResponse;
import com.openmpy.server.document.application.response.DocumentGetResponse;
import com.openmpy.server.document.application.response.DocumentImageUploadResponse;
import com.openmpy.server.document.application.response.DocumentImageUploadResponses;
import com.openmpy.server.document.application.response.DocumentPageResponse;
import com.openmpy.server.document.application.response.DocumentUpdateResponse;
import com.openmpy.server.document.domain.constants.DocumentCategory;
import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.entity.DocumentHistory;
import com.openmpy.server.document.domain.entity.DocumentImage;
import com.openmpy.server.document.domain.repository.DocumentHistoryRepository;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.global.dto.response.PageResponse;
import com.openmpy.server.global.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private static final String DOCUMENT_CATEGORY_ALL = "all";

    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;
    private final DocumentImageRepository documentImageRepository;
    private final ImageStorage imageStorage;

    @Transactional
    public DocumentCreateResponse create(final DocumentCreateRequest request, final HttpServletRequest servletRequest) {
        validateDuplicate(request);

        final Document document = Document.create(
                request.title(),
                request.category(),
                request.author(),
                request.content(),
                getContentByteSize(request.content()),
                ClientIpUtil.getClientIp(servletRequest)
        );
        final Document savedDocument = documentRepository.save(document);

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

        document.addHistory(
                request.author(),
                request.content(),
                getContentByteSize(request.content()),
                ClientIpUtil.getClientIp(servletRequest)
        );
        attachImages(document, request.imageIds());
        return new DocumentUpdateResponse(document.getId());
    }

    @Transactional(readOnly = true)
    public DocumentGetResponse getLatest(final Long documentId) {
        final Document document = findDocumentById(documentId);

        return DocumentGetResponse.from(document, document.getLastHistory());
    }

    @Transactional(readOnly = true)
    public PageResponse<DocumentPageResponse> getLatestDocuments(
            final String category,
            final int page,
            final int size
    ) {
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

        if (category.equalsIgnoreCase(DOCUMENT_CATEGORY_ALL)) {
            final Page<Document> documentPage = documentRepository.findAll(pageRequest);

            return convertToDocumentPageResponse(documentPage);
        }

        final DocumentCategory selectedCategory = DocumentCategory.valueOf(category.toUpperCase());
        final Page<Document> documentPage = documentRepository.findAllByCategory(selectedCategory, pageRequest);

        return convertToDocumentPageResponse(documentPage);
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

    @Transactional
    public DocumentImageUploadResponses uploadImages(
            final List<MultipartFile> images,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpUtil.getClientIp(servletRequest);
        final List<DocumentImageUploadResponse> responses = images.stream()
                .map(it -> {
                    final UploadedImage uploadedImage = imageStorage.upload(it);
                    final DocumentImage documentImage = DocumentImage.create(uploadedImage.url(), clientIp);
                    final DocumentImage savedDocumentImage = documentImageRepository.save(documentImage);

                    return new DocumentImageUploadResponse(savedDocumentImage.getId(), uploadedImage.url());
                })
                .toList();

        return new DocumentImageUploadResponses(responses);
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

    private PageResponse<DocumentPageResponse> convertToDocumentPageResponse(final Page<Document> documentPage) {
        final List<DocumentPageResponse> documentResponses = documentPage.getContent().stream()
                .map(DocumentPageResponse::from)
                .toList();

        return PageResponse.of(
                documentResponses,
                documentPage.getNumber(),
                documentPage.getSize(),
                documentPage.getTotalElements()
        );
    }

    private void attachImages(final Document document, final List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) {
            return;
        }

        final List<DocumentImage> images = documentImageRepository.findAllById(imageIds);

        document.attachImages(images);
    }
}
