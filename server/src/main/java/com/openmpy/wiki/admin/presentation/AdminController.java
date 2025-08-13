package com.openmpy.wiki.admin.presentation;

import com.openmpy.wiki.admin.application.AdminService;
import com.openmpy.wiki.admin.application.request.AdminLoginRequest;
import com.openmpy.wiki.global.utils.ClientIpExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody final AdminLoginRequest request, final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpExtractor.getClientIp(servletRequest);
        adminService.login(request, clientIp);
        return ResponseEntity.noContent().build();
    }
}
