package com.openmpy.server.document.application.command;

import com.openmpy.server.document.application.usecase.DeleteAllDocumentImagesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentImageScheduler {

    private final DeleteAllDocumentImagesUseCase deleteAllDocumentImagesUseCase;

    @Scheduled(cron = "0 0 0 * * ?")
    public void execute() {
        final long count = deleteAllDocumentImagesUseCase.execute();

        log.info("문서 이미지가 삭제되었습니다. {}개", count);
    }
}
