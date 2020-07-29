package com.kuaidaoresume.common.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix="kuaidaoresume.common")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KuaidaoresumeProps {
    @NotBlank
    private String sentryDsn;
    @NotBlank
    // DeployEnvVar is set by Kubernetes during a new deployment so we can identify the code version
    private String deployEnv;
}
