package com.openmpy.wiki.document.application;

import com.openmpy.wiki.document.application.request.DocumentModerateRequest;
import com.openmpy.wiki.document.application.response.DocumentModerateResultResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class DocumentModerationServiceTest {

    @Autowired
    DocumentModerationService documentModerationService;

    @Test
    void document_moderation_service_test_01() {
        // given
        final DocumentModerateRequest request = new DocumentModerateRequest(
                "omni-moderation-latest",
                "바보 멍청이 ㅋㅋ"
        );

        // when
        final DocumentModerateResultResponse response = documentModerationService.getViolenceScore(request);

        // then
        System.out.println("response = " + response);
    }
}