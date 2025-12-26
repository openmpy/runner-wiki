package com.openmpy.server.document.application;

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
import com.openmpy.server.global.properties.S3Properties;
import com.openmpy.server.global.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private static final String DOCUMENT_CATEGORY_ALL = "all";

    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;
    private final DocumentImageRepository documentImageRepository;
    private final S3Properties s3Properties;
    private final S3Client s3Client;

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

        assignImagesToDocument(request.imageIds(), savedDocument);
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
        assignImagesToDocument(request.imageIds(), document);
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
        final List<DocumentImageUploadResponse> responses = new ArrayList<>();

        for (final MultipartFile image : images) {
            final String originalFilename = image.getOriginalFilename();
            final String extension = StringUtils.getFilenameExtension(originalFilename);

            try {
                final String key = UUID.randomUUID() + "." + extension;
                final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(s3Properties.bucket())
                        .key(key)
                        .contentType(image.getContentType())
                        .build();

                s3Client.putObject(
                        putObjectRequest,
                        RequestBody.fromInputStream(image.getInputStream(), image.getSize())
                );

                final String url = s3Properties.endpoint() + "/" + s3Properties.bucket() + "/" + key;
                final DocumentImage documentImage = DocumentImage.create(url, ClientIpUtil.getClientIp(servletRequest));
                final DocumentImage savedDocumentImage = documentImageRepository.save(documentImage);
                final DocumentImageUploadResponse response = new DocumentImageUploadResponse(
                        savedDocumentImage.getId(),
                        savedDocumentImage.getUrl()
                );

                responses.add(response);
            } catch (final Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        return new DocumentImageUploadResponses(responses);
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

    private void assignImagesToDocument(final List<Long> imageIds, final Document document) {
        if (imageIds != null) {
            for (final Long imageId : imageIds) {
                documentImageRepository.findById(imageId).ifPresent(document::assignImage);
            }
        }
    }
}
