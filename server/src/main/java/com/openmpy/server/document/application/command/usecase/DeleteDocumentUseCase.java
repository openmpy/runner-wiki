package com.openmpy.server.document.application.command.usecase;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import com.openmpy.server.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteDocumentUseCase {

    private final DocumentRepository documentRepository;

    @Transactional
    public void execute(final Long documentId) {
        final Document document = documentRepository.findById(documentId).orElseThrow(
                () -> new CustomException("찾을 수 없는 문서 번호입니다.")
        );

        document.delete();
    }
}
