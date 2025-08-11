package com.openmpy.wiki.view.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmpy.wiki.global.exception.CustomException;
import com.openmpy.wiki.view.application.event.ViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ViewProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void viewDocument(final String documentId, final String clientIp) {
        final ViewEvent viewEvent = new ViewEvent(documentId, clientIp);
        try {
            final String json = objectMapper.writeValueAsString(viewEvent);
            kafkaTemplate.send("document-views", json);
        } catch (final JsonProcessingException e) {
            throw new CustomException("view document event json parsing error: " + e.getMessage());
        }
    }
}
