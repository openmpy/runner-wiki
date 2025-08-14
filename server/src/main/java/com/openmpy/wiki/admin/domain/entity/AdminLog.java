package com.openmpy.wiki.admin.domain.entity;

import com.openmpy.wiki.admin.domain.constants.AdminLogType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "admin_log")
public class AdminLog {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminLogType logType;

    @Column
    private String content;

    @Column(nullable = false)
    private String clientIp;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static AdminLog create(final String id, final AdminLogType logType, final String clientIp) {
        final AdminLog adminLog = new AdminLog();
        adminLog.id = id;
        adminLog.logType = logType;
        adminLog.clientIp = clientIp;
        adminLog.createdAt = LocalDateTime.now();
        return adminLog;
    }

    public static AdminLog create(
            final String id, final AdminLogType logType, final String content, final String clientIp
    ) {
        final AdminLog adminLog = new AdminLog();
        adminLog.id = id;
        adminLog.logType = logType;
        adminLog.content = content;
        adminLog.clientIp = clientIp;
        adminLog.createdAt = LocalDateTime.now();
        return adminLog;
    }
}
