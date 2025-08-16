package com.openmpy.wiki.document.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmpy.wiki.document.application.request.DocumentModerateEvent;
import com.openmpy.wiki.document.application.request.DocumentModerateRequest;
import com.openmpy.wiki.document.application.response.DocumentModerateResultResponse;
import com.openmpy.wiki.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DocumentConsumer {

    private static final String DOCUMENT_MODERATION_MODEL = "omni-moderation-latest";

    private final ObjectMapper objectMapper;
    private final DocumentModerationService documentModerationService;
    private final DocumentService documentService;

    @KafkaListener(topics = "document-moderation", groupId = "document")
    public void moderationConsume(final String message) {
        try {
            final DocumentModerateEvent event = objectMapper.readValue(message, DocumentModerateEvent.class);
            final DocumentModerateRequest request = new DocumentModerateRequest(
                    DOCUMENT_MODERATION_MODEL, event.content()
            );

            final DocumentModerateResultResponse response = documentModerationService.getViolenceScore(request);
            documentService.updateHistoryScore(event.documentHistoryId(), response.score());
        } catch (final JsonProcessingException e) {
            throw new CustomException("document moderation event json parsing error: " + e.getMessage());
        }
    }
}
