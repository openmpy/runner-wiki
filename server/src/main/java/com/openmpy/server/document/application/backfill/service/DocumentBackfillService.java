package com.openmpy.server.document.application.backfill.service;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.global.util.KoreanChosung;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentBackfillService {

    private final DocumentRepository documentRepository;

    @Transactional
    public long chosungBackfill(final int batchSize) {
        long updated = 0L;

        while (true) {
            final Page<Document> chunk = documentRepository.findByTitleChosungIsNullOrTitleChosungEquals(
                    "", PageRequest.of(0, batchSize)
            );

            if (chunk.isEmpty()) {
                break;
            }

            for (final Document document : chunk.getContent()) {
                final String chosung = KoreanChosung.toChosung(document.getTitle());

                updated += documentRepository.updateTitleChosungOnly(document.getId(), chosung);
            }
        }
        return updated;
    }
}
