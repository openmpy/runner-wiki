package com.openmpy.server.document.infrastructure.scheduler;

import com.openmpy.server.document.application.DocumentImageCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentImageScheduler {

    private final DocumentImageCommandService documentImageCommandService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void execute() {
        final long count = documentImageCommandService.deleteTempImages();

        log.info("문서 이미지가 삭제되었습니다. {}개", count);
    }
}
