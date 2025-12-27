package com.openmpy.server.document.application.support;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.model.DocumentImage;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import com.openmpy.server.document.domain.type.DocumentImageStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ImageAttacher {

    private final DocumentImageRepository documentImageRepository;

    public void attachTempImages(final Document document, final List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) {
            return;
        }

        final List<DocumentImage> images = documentImageRepository.findAllByIdInAndStatus(
                imageIds,
                DocumentImageStatus.TEMP
        );

        if (images.size() != imageIds.size()) {
            throw new IllegalArgumentException("이미지 업로드 갯수가 올바르지 않습니다.");
        }

        document.attachImages(images);
    }
}
