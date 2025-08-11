package com.openmpy.wiki.view.presentation;

import com.openmpy.wiki.global.utils.ClientIpExtractor;
import com.openmpy.wiki.view.application.ViewProducer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ViewController {

    private final ViewProducer viewProducer;

    @PostMapping("/documents/{documentId}/view")
    public ResponseEntity<Void> viewDocument(
            @PathVariable final String documentId,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        viewProducer.viewDocument(documentId, clientIp);
        return ResponseEntity.noContent().build();
    }
}
