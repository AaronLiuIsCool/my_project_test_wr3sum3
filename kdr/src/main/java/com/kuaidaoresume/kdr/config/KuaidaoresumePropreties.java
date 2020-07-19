package com.kuaidaoresume.kdr.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix="kuaidaoresume")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KuaidaoresumePropreties {
  @NotNull
  private String signingSecret;
}
