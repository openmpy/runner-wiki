package com.openmpy.wiki.view.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmpy.wiki.global.exception.CustomException;
import com.openmpy.wiki.view.application.event.ViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ViewConsumer {

    private final ViewService viewService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "document-views", groupId = "view-counter")
    public void consume(final String message) {
        try {
            final ViewEvent viewEvent = objectMapper.readValue(message, ViewEvent.class);
            viewService.incrementViewCount(viewEvent.documentId(), viewEvent.clientIp());
        } catch (final JsonProcessingException e) {
            throw new CustomException("view document event json parsing error: " + e.getMessage());
        }
    }
}
