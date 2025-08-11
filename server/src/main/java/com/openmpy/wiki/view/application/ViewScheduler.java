package com.openmpy.wiki.view.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ViewScheduler {

    private final ViewService viewService;

    @Scheduled(cron = "0 */10 * * * *")
    public void syncDocumentViewCount() {
        viewService.syncDocumentViewCount();
    }
}
