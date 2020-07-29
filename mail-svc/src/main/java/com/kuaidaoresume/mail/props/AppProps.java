package com.kuaidaoresume.mail.props;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix="kuaidaoresume")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AppProps {

    // AWS directmail props
    @NotNull
    private String AWSAccessKey;

    @NotNull
    private String AWSAccessSecret;
}
