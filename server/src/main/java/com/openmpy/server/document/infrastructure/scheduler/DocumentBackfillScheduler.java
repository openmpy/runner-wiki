package com.openmpy.server.document.infrastructure.scheduler;

import com.openmpy.server.document.application.backfill.service.DocumentBackfillService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentBackfillScheduler {

    private static final int BATCH_SIZE = 1000;

    private final DocumentBackfillService documentBackfillService;

    @Scheduled(cron = "0 0 * * * ?")
    public void titleChosungExecute() {
        documentBackfillService.chosungBackfill(BATCH_SIZE);
    }
}
