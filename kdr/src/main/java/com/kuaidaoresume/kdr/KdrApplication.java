package com.kuaidaoresume.kdr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class KdrApplication {
  public static void main(String[] args) {
    SpringApplication.run(KdrApplication.class, args);
  }

  @Value("${kdr.cors.enabled:false}")
  private boolean CORS_ENABLED;
  @Value("${kdr.cors.webapp_origin:@null}")
  private String CORS_WEBAPP_ORIGIN;

  // https://stackoverflow.com/questions/42874351/spring-boot-enabling-cors-by-application-properties/42899222
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        if (!CORS_ENABLED || CORS_WEBAPP_ORIGIN == null) return;

        registry.addMapping("/**")
                .allowedOrigins(CORS_WEBAPP_ORIGIN.split(","))
                .allowCredentials(true);
      }
    };
  }
}
