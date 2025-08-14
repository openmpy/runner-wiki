package com.openmpy.wiki.admin.application;

import com.openmpy.wiki.admin.application.request.AdminLoginRequest;
import com.openmpy.wiki.admin.application.response.AdminLoginResponse;
import com.openmpy.wiki.admin.domain.constants.AdminLogType;
import com.openmpy.wiki.admin.domain.entity.AdminLog;
import com.openmpy.wiki.admin.domain.repository.AdminLogRepository;
import com.openmpy.wiki.auth.application.JwtService;
import com.openmpy.wiki.global.exception.CustomException;
import com.openmpy.wiki.global.properties.AdminProperties;
import com.openmpy.wiki.global.snowflake.Snowflake;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final Snowflake snowflake = new Snowflake();
    private final AdminProperties adminProperties;
    private final AdminLogRepository adminLogRepository;
    private final JwtService jwtService;

    @Transactional
    public AdminLoginResponse login(final AdminLoginRequest request, final String clientIp) {
        if (!adminProperties.id().equals(request.id()) || !adminProperties.password().equals(request.password())) {
            throw new CustomException("어드민 아이디 또는 패스워드가 일치하지 않습니다.");
        }

        final String id = snowflake.nextId();
        final AdminLog adminLog = AdminLog.create(id, AdminLogType.LOGIN, clientIp);
        adminLogRepository.save(adminLog);

        return new AdminLoginResponse(generateToken(id));
    }

    @Transactional
    public void updateDocumentStatus(final String documentId, final String clientIp) {
        final String id = snowflake.nextId();
        final AdminLog adminLog = AdminLog.create(id, AdminLogType.UPDATE_DOCUMENT_STATUS, documentId, clientIp);
        adminLogRepository.save(adminLog);
    }

    @Transactional
    public void deleteDocument(final String documentId, final String clientIp) {
        final String id = snowflake.nextId();
        final AdminLog adminLog = AdminLog.create(id, AdminLogType.DELETE_DOCUMENT, documentId, clientIp);
        adminLogRepository.save(adminLog);
    }

    @Transactional
    public void deleteDocumentHistory(final String documentHistoryId, final String clientIp) {
        final String id = snowflake.nextId();
        final AdminLog adminLog = AdminLog.create(
                id, AdminLogType.DELETE_DOCUMENT_HISTORY, documentHistoryId, clientIp
        );
        adminLogRepository.save(adminLog);
    }

    private String generateToken(final String id) {
        final Map<String, Object> claims = Map.of(
                "id", id,
                "role", "admin"
        );
        return jwtService.createToken(claims);
    }
}
