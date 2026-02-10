package com.openmpy.server.document.application.command.usecase;

import static com.openmpy.server.document.domain.type.DocumentImageStatus.TEMP;

import com.openmpy.server.document.application.image.port.ImageStorage;
import com.openmpy.server.document.domain.entity.DocumentImage;
import com.openmpy.server.document.domain.repository.DocumentImageRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteAllDocumentImagesUseCase {

    private final DocumentImageRepository documentImageRepository;
    private final ImageStorage imageStorage;

    @Transactional
    public long execute() {
        final List<DocumentImage> documentImages = documentImageRepository.findAllByStatusAndExpiredAtBefore(
            TEMP,
            LocalDateTime.now()
        );
        long deleted = 0;

        for (final DocumentImage documentImage : documentImages) {
            try {
                imageStorage.delete(documentImage.getUrl());
                documentImageRepository.delete(documentImage);
                deleted++;
            } catch (final Exception e) {
                throw new IllegalStateException("이미지 삭제 실패", e);
            }
        }
        return deleted;
    }
}
