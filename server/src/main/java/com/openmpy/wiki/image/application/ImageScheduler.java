package com.openmpy.wiki.image.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImageScheduler {

    private final ImageService imageService;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanUp() {
        imageService.cleanUp();
    }
}
