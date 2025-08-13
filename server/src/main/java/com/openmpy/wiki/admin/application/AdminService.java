package com.openmpy.wiki.admin.application;

import com.openmpy.wiki.admin.application.request.AdminLoginRequest;
import com.openmpy.wiki.admin.domain.constants.AdminLogType;
import com.openmpy.wiki.admin.domain.entity.AdminLog;
import com.openmpy.wiki.admin.domain.repository.AdminLogRepository;
import com.openmpy.wiki.global.exception.CustomException;
import com.openmpy.wiki.global.properties.AdminProperties;
import com.openmpy.wiki.global.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final Snowflake snowflake = new Snowflake();
    private final AdminProperties adminProperties;
    private final AdminLogRepository adminLogRepository;

    @Transactional
    public void login(final AdminLoginRequest request, final String clientIp) {
        if (!adminProperties.id().equals(request.id()) || !adminProperties.password().equals(request.password())) {
            throw new CustomException("어드민 아이디 또는 패스워드가 일치하지 않습니다.");
        }

        final AdminLog adminLog = AdminLog.create(snowflake.nextId(), AdminLogType.LOGIN, clientIp);
        adminLogRepository.save(adminLog);

        // TODO: Access Token 발급
    }
}
