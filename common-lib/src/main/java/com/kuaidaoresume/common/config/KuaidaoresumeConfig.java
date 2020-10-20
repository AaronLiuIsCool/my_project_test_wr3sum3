package com.kuaidaoresume.common.config;

import com.github.structlog4j.StructLog4J;
import com.github.structlog4j.json.JsonFormatter;
import feign.RequestInterceptor;
import io.sentry.Sentry;
import io.sentry.SentryClient;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.kuaidaoresume.common.auth.AuthorizeInterceptor;
import com.kuaidaoresume.common.auth.FeignRequestHeaderInterceptor;
import com.kuaidaoresume.common.env.EnvConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Configuration
@EnableConfigurationProperties(KuaidaoresumeProps.class)
public class KuaidaoresumeConfig implements WebMvcConfigurer {

    @Value("${spring.profiles.active:NA}")
    private String activeProfile;

    @Value("${spring.application.name:NA}")
    private String appName;

    @Autowired
    KuaidaoresumeProps kuaidaoresumeProps;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelmapper = new ModelMapper();

        Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
            @Override
            public LocalDate get() {
                return LocalDate.now();
            }
        };

        Converter<String, LocalDate> stringToLocalDateConverter = new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String source) {
                if (Objects.isNull(source)) return null;
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(source, format);
                return localDate;
            }
        };

        modelmapper.createTypeMap(String.class, LocalDate.class);
        modelmapper.addConverter(stringToLocalDateConverter);
        modelmapper.getTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);
        modelmapper.getConfiguration().setSkipNullEnabled(true);
        return modelmapper;
    }

    @Bean
    public EnvConfig envConfig() {
        return EnvConfig.getEnvConfg(activeProfile);
    }

    @Bean
    public SentryClient sentryClient() {

        SentryClient sentryClient = Sentry.init(kuaidaoresumeProps.getSentryDsn());
        sentryClient.setEnvironment(activeProfile);
        sentryClient.setRelease(kuaidaoresumeProps.getDeployEnv());
        sentryClient.addTag("service", appName);

        return sentryClient;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizeInterceptor());
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return new FeignRequestHeaderInterceptor();
    }

    @PostConstruct
    public void init() {
        // init structured logging
        StructLog4J.setFormatter(JsonFormatter.getInstance());

        // global log fields setting
        StructLog4J.setMandatoryContextSupplier(() -> new Object[]{
                "env", activeProfile,
                "service", appName});
    }

    @PreDestroy
    public void destroy() {
        sentryClient().closeConnection();
    }
}
