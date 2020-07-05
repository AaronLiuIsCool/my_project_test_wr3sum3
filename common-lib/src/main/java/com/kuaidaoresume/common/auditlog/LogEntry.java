package com.kuaidaoresume.common.auditlog;

import com.github.structlog4j.IToLog;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogEntry implements IToLog {

    private String currentUserId;

    @Override
    public Object[] toLog() {
        return new Object[] {
                "auditlog", "true"
        };
    }
}