package com.openmpy.wiki.document.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmpy.wiki.document.application.request.DocumentModerateEvent;
import com.openmpy.wiki.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DocumentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void moderateDocumentHistory(final String documentHistoryId, final String content) {
        try {
            final DocumentModerateEvent event = new DocumentModerateEvent(documentHistoryId, content);
            final String json = objectMapper.writeValueAsString(event);

            kafkaTemplate.send("document-moderation", json);
        } catch (final JsonProcessingException e) {
            throw new CustomException("document moderation event json parsing error: " + e.getMessage());
        }
    }
}
