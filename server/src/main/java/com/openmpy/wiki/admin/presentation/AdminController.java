package com.openmpy.wiki.admin.presentation;

import static com.openmpy.wiki.auth.application.JwtService.ACCESS_TOKEN;

import com.openmpy.wiki.admin.application.AdminService;
import com.openmpy.wiki.admin.application.request.AdminLoginRequest;
import com.openmpy.wiki.admin.application.response.AdminDocumentHistoryReadResponse;
import com.openmpy.wiki.admin.application.response.AdminLoginResponse;
import com.openmpy.wiki.document.application.DocumentFacade;
import com.openmpy.wiki.document.application.DocumentService;
import com.openmpy.wiki.document.application.response.DocumentReadResponse;
import com.openmpy.wiki.global.dto.PageResponse;
import com.openmpy.wiki.global.utils.ClientIpExtractor;
import com.openmpy.wiki.global.utils.CookieManager;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/health")
    public ResponseEntity<Void> health() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(
            @RequestBody final AdminLoginRequest request, final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        final AdminLoginResponse response = adminService.login(request, clientIp);
        final ResponseCookie cookie = CookieManager.createCookie(ACCESS_TOKEN, response.jwt(), domain);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        final ResponseCookie cookie = CookieManager.deleteCooke(ACCESS_TOKEN, domain);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PatchMapping("/documents/{documentId}")
    public ResponseEntity<Void> updateDocumentStatus(
            @PathVariable final String documentId,
            @RequestParam final String status,
            final HttpServletRequest servletRequest
    ) {
        documentService.updateDocumentStatus(documentId, status);

        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        adminService.updateDocumentStatus(documentId, clientIp);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable final String documentId,
            final HttpServletRequest servletRequest
    ) {
        documentFacade.deleteDocument(documentId);

        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        adminService.deleteDocument(documentId, clientIp);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/documents/histories/{documentHistoryId}")
    public ResponseEntity<Void> updateDocumentHistoryStatus(
            @PathVariable final String documentHistoryId,
            @RequestParam final String status,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        adminService.updateDocumentHistory(documentHistoryId, status, clientIp);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/documents")
    public ResponseEntity<PageResponse<List<DocumentReadResponse>>> readDocuments(
            @RequestParam(value = "title", defaultValue = "") final String title,
            @RequestParam("page") final int page,
            @RequestParam(value = "size", defaultValue = "100", required = false) final int size
    ) {
        return ResponseEntity.ok(documentService.readDocuments(title, page, size));
    }

    @GetMapping("/documents/histories")
    public ResponseEntity<PageResponse<List<AdminDocumentHistoryReadResponse>>> readDocumentHistories(
            @RequestParam(value = "title", defaultValue = "") final String title,
            @RequestParam("page") final int page,
            @RequestParam(value = "size", defaultValue = "100", required = false) final int size
    ) {
        return ResponseEntity.ok(documentService.readDocumentHistoryAdmin(title, page, size));
    }
}
