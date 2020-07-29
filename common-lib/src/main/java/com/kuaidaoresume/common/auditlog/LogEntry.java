package com.kuaidaoresume.common.auditlog;

import com.github.structlog4j.IToLog;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogEntry implements IToLog {

    private String currentUserId;
    private String resumeId;
    private String jobId;
    private String authorization;
    private String targetType;
    private String targetId;
    private String originalContents;
    private String updatedContents;

    @Override
    public Object[] toLog() {
        return new Object[] {
                "auditlog", "true",
                "resumeId", resumeId,
                "jobId", jobId,
                "authorization", authorization,
                "targetType", targetType,
                "targetId", targetId,
                "originalContents", originalContents,
                "updatedContents", updatedContents
        };
    }
}