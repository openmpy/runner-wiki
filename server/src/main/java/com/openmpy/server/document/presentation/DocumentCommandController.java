package com.openmpy.server.document.presentation;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.openmpy.server.document.application.DocumentCommandService;
import com.openmpy.server.document.application.DocumentHistoryCommandService;
import com.openmpy.server.document.application.DocumentImageCommandService;
import com.openmpy.server.document.application.DocumentRankingCommandService;
import com.openmpy.server.document.dto.request.DocumentCreateRequest;
import com.openmpy.server.document.dto.request.DocumentUpdateRequest;
import com.openmpy.server.document.dto.response.DocumentCreateResponse;
import com.openmpy.server.document.dto.response.DocumentUpdateResponse;
import com.openmpy.server.global.util.ClientIpResolver;
import com.openmpy.server.image.dto.ImagePresignRequest;
import com.openmpy.server.image.dto.ImagePresignResponse;
import com.openmpy.server.verifier.application.TurnstileVerifierAdapter;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class DocumentCommandController {

    private final DocumentCommandService documentCommandService;
    private final DocumentHistoryCommandService documentHistoryCommandService;
    private final DocumentImageCommandService documentImageCommandService;
    private final DocumentRankingCommandService documentRankingCommandService;
    private final TurnstileVerifierAdapter turnstileVerifierAdapter;

    @Value("${admin.password}")
    private String password;

    @PostMapping("/v1/documents")
    public ResponseEntity<DocumentCreateResponse> save(
        @RequestBody final DocumentCreateRequest request,
        final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpResolver.getClientIp(servletRequest);
        final boolean isVerified = turnstileVerifierAdapter.verify(request.token(), clientIp);

        if (!isVerified) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(documentCommandService.save(request, clientIp));
    }

    @PutMapping("/v1/documents/{documentId}")
    public ResponseEntity<DocumentUpdateResponse> update(
        @PathVariable final Long documentId,
        @RequestBody final DocumentUpdateRequest request,
        final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpResolver.getClientIp(servletRequest);
        final boolean isVerified = turnstileVerifierAdapter.verify(request.token(), clientIp);

        if (!isVerified) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(documentCommandService.update(documentId, request, clientIp));
    }

    @DeleteMapping("/v1/documents/{documentId}")
    public ResponseEntity<Void> delete(
        @RequestHeader("password") final String password,
        @PathVariable final Long documentId
    ) {
        validatePassword(password);

        documentCommandService.delete(documentId);
        documentRankingCommandService.removeFromRanking(documentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/v1/document-histories/{historyId}")
    public ResponseEntity<Void> deleteHistory(
        @RequestHeader("password") final String password,
        @PathVariable final Long historyId
    ) {
        validatePassword(password);

        documentHistoryCommandService.delete(historyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/document-images/presign")
    public ResponseEntity<ImagePresignResponse> createPresignedImage(
        @RequestBody final ImagePresignRequest request
    ) {
        final ImagePresignResponse response = documentImageCommandService.createPresignedImage(
            request
        );

        return ResponseEntity.ok(response);
    }

    private void validatePassword(final String password) {
        if (!this.password.equals(password)) {
            throw new ResponseStatusException(FORBIDDEN, "비밀번호가 올바르지 않습니다.");
        }
    }
}
