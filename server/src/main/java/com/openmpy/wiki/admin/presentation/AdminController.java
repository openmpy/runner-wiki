package com.openmpy.wiki.admin.presentation;

import static com.openmpy.wiki.auth.application.JwtService.ACCESS_TOKEN;

import com.openmpy.wiki.admin.application.AdminService;
import com.openmpy.wiki.admin.application.request.AdminLoginRequest;
import com.openmpy.wiki.admin.application.response.AdminLoginResponse;
import com.openmpy.wiki.document.application.DocumentFacade;
import com.openmpy.wiki.document.application.DocumentService;
import com.openmpy.wiki.global.utils.ClientIpExtractor;
import com.openmpy.wiki.global.utils.CookieManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminController {

    private final AdminService adminService;
    private final DocumentService documentService;
    private final DocumentFacade documentFacade;

    @Value("${cookie.domain}")
    private String domain;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(
            @RequestBody final AdminLoginRequest request, final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        final AdminLoginResponse response = adminService.login(request, clientIp);
        final ResponseCookie cookie = CookieManager.createCookie(ACCESS_TOKEN, response.jwt(), domain);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable final String documentId,
            final HttpServletRequest servletRequest
    ) {
        documentFacade.deleteDocument(documentId);

        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        adminService.deleteDocument(documentId, clientIp);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/documents/histories/{documentHistoryId}")
    public ResponseEntity<Void> deleteDocumentHistory(
            @PathVariable final String documentHistoryId,
            final HttpServletRequest servletRequest
    ) {
        documentService.deleteDocumentHistory(documentHistoryId);

        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        adminService.deleteDocumentHistory(documentHistoryId, clientIp);
        return ResponseEntity.ok().build();
    }
}
