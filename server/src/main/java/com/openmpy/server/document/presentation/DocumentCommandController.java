package com.openmpy.server.document.presentation;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.openmpy.server.document.application.DocumentCommandService;
import com.openmpy.server.document.application.DocumentHistoryCommandService;
import com.openmpy.server.document.application.DocumentImageCommandService;
import com.openmpy.server.document.application.command.dto.request.DocumentCreateRequest;
import com.openmpy.server.document.application.command.dto.request.DocumentUpdateRequest;
import com.openmpy.server.document.application.command.dto.response.DocumentCreateResponse;
import com.openmpy.server.document.application.command.dto.response.DocumentImageUploadResponses;
import com.openmpy.server.document.application.command.dto.response.DocumentUpdateResponse;
import com.openmpy.server.document.application.ranking.service.DocumentRankingCommandService;
import com.openmpy.server.document.infrastructure.turnstile.TurnstileVerifier;
import com.openmpy.server.global.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class DocumentCommandController {

    private final DocumentCommandService documentCommandService;
    private final DocumentHistoryCommandService documentHistoryCommandService;
    private final DocumentImageCommandService documentImageCommandService;
    private final DocumentRankingCommandService documentRankingCommandService;
    private final TurnstileVerifier turnstileVerifier;

    @Value("${admin.password}")
    private String password;

    @PostMapping("/documents")
    public ResponseEntity<DocumentCreateResponse> save(
        @RequestBody final DocumentCreateRequest request,
        final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpUtil.getClientIp(servletRequest);
        final boolean isVerified = turnstileVerifier.verify(request.token(), clientIp);

        if (!isVerified) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(documentCommandService.save(request, clientIp));
    }

    @PutMapping("/documents/{documentId}")
    public ResponseEntity<DocumentUpdateResponse> update(
        @PathVariable final Long documentId,
        @RequestBody final DocumentUpdateRequest request,
        final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpUtil.getClientIp(servletRequest);
        final boolean isVerified = turnstileVerifier.verify(request.token(), clientIp);

        if (!isVerified) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(documentCommandService.update(documentId, request, clientIp));
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> delete(
        @RequestHeader("password") final String password,
        @PathVariable final Long documentId
    ) {
        validatePassword(password);

        documentCommandService.delete(documentId);
        documentRankingCommandService.removeFromRanking(documentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/document-histories/{historyId}")
    public ResponseEntity<Void> deleteHistory(
        @RequestHeader("password") final String password,
        @PathVariable final Long historyId
    ) {
        validatePassword(password);

        documentHistoryCommandService.delete(historyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/document-images")
    public ResponseEntity<DocumentImageUploadResponses> uploadImages(
        @RequestBody final List<MultipartFile> images,
        final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpUtil.getClientIp(servletRequest);
        final DocumentImageUploadResponses responses = documentImageCommandService.uploadImages(
            images, clientIp
        );

        return ResponseEntity.ok(responses);
    }

    private void validatePassword(final String password) {
        if (!this.password.equals(password)) {
            throw new ResponseStatusException(FORBIDDEN, "비밀번호가 올바르지 않습니다.");
        }
    }
}
