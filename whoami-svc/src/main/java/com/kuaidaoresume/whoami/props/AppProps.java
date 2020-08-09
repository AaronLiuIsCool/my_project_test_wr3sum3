package com.kuaidaoresume.whoami.props;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix="kuaidaoresume")
@Data
@Builder
@NoArgsConstructor
//@AllArgsConstructor
public class AppProps {

    //private String intercomAppId;
    //private String intercomSigningSecret;
}
