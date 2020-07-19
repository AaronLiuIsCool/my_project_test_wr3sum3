package com.kuaidaoresume.kdr.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import com.kuaidaoresume.common.config.KuaidaoresumeWebConfig;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.kdr.core.filter.FaviconFilter;
import com.kuaidaoresume.kdr.core.filter.HealthCheckFilter;
import com.kuaidaoresume.kdr.core.filter.NakedDomainFilter;
import com.kuaidaoresume.kdr.core.filter.SecurityFilter;
import com.kuaidaoresume.kdr.core.interceptor.*;
import com.kuaidaoresume.kdr.core.balancer.LoadBalancer;
import com.kuaidaoresume.kdr.core.balancer.RandomLoadBalancer;
import com.kuaidaoresume.kdr.core.http.*;
import com.kuaidaoresume.kdr.core.mappings.ConfigurationMappingsProvider;
import com.kuaidaoresume.kdr.core.mappings.MappingsProvider;
import com.kuaidaoresume.kdr.core.mappings.MappingsValidator;
import com.kuaidaoresume.kdr.core.mappings.ProgrammaticMappingsProvider;
import com.kuaidaoresume.kdr.core.trace.LoggingTraceInterceptor;
import com.kuaidaoresume.kdr.core.trace.ProxyingTraceInterceptor;
import com.kuaidaoresume.kdr.core.trace.TraceInterceptor;
import com.kuaidaoresume.kdr.view.AssetLoader;

import java.util.*;

@Configuration
@EnableConfigurationProperties({KdrProperties.class, KuaidaoresumePropreties.class})
@Import(value = KuaidaoresumeWebConfig.class)
public class KdrConfiguration {

  protected final KdrProperties kdrProperties;
  protected final ServerProperties serverProperties;
  protected final KuaidaoresumePropreties kuaidaoresumePropreties;
  protected final AssetLoader assetLoader;

  public KdrConfiguration(KdrProperties kdrProperties,
                          ServerProperties serverProperties,
                          KuaidaoresumePropreties kuaidaoresumePropreties,
                          AssetLoader assetLoader) {
    this.kdrProperties = kdrProperties;
    this.serverProperties = serverProperties;
    this.kuaidaoresumePropreties = kuaidaoresumePropreties;
    this.assetLoader = assetLoader;
  }

  @Bean
  public FilterRegistrationBean<ReverseProxyFilter> kdrReverseProxyFilterRegistrationBean(
    ReverseProxyFilter proxyFilter) {
    FilterRegistrationBean<ReverseProxyFilter> registrationBean = new FilterRegistrationBean<>(proxyFilter);
    registrationBean.setOrder(kdrProperties.getFilterOrder()); // by default to Ordered.HIGHEST_PRECEDENCE + 100
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<NakedDomainFilter> nakedDomainFilterRegistrationBean(EnvConfig envConfig) {
    FilterRegistrationBean<NakedDomainFilter> registrationBean =
      new FilterRegistrationBean<>(new NakedDomainFilter(envConfig));
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 90); // before ReverseProxyFilter
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<SecurityFilter> securityFilterRegistrationBean(EnvConfig envConfig) {
    FilterRegistrationBean<SecurityFilter> registrationBean =
      new FilterRegistrationBean<>(new SecurityFilter(envConfig));
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 80); // before nakedDomainFilter
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<FaviconFilter> faviconFilterRegistrationBean() {
    FilterRegistrationBean<FaviconFilter> registrationBean =
      new FilterRegistrationBean<>(new FaviconFilter(assetLoader.getFaviconFile()));
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 75); // before securityFilter
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<HealthCheckFilter> healthCheckFilterRegistrationBean() {
    FilterRegistrationBean<HealthCheckFilter> registrationBean =
      new FilterRegistrationBean<>(new HealthCheckFilter());
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 70); // before faviconFilter
    return registrationBean;
  }

  @Bean
  @ConditionalOnMissingBean
  public ReverseProxyFilter kdrReverseProxyFilter(
    RequestDataExtractor extractor,
    MappingsProvider mappingsProvider,
    RequestForwarder requestForwarder,
    ProxyingTraceInterceptor traceInterceptor,
    PreForwardRequestInterceptor requestInterceptor
  ) {
    return new ReverseProxyFilter(kdrProperties, extractor, mappingsProvider,
      requestForwarder, traceInterceptor, requestInterceptor);
  }

  @Bean
  @ConditionalOnMissingBean
  public HttpClientProvider kdrHttpClientProvider() {
    return new HttpClientProvider();
  }

  @Bean
  @ConditionalOnMissingBean
  public RequestDataExtractor kdrRequestDataExtractor() {
    return new RequestDataExtractor();
  }

  @Bean
  @ConditionalOnMissingBean
  public MappingsProvider kdrConfigurationMappingsProvider(EnvConfig envConfig,
                                                               MappingsValidator mappingsValidator,
                                                               HttpClientProvider httpClientProvider) {
    if (kdrProperties.isEnableProgrammaticMapping()) {
      return new ProgrammaticMappingsProvider(
        envConfig, serverProperties,
        kdrProperties, mappingsValidator,
        httpClientProvider);
    } else {
      return new ConfigurationMappingsProvider(
        serverProperties,
        kdrProperties, mappingsValidator,
        httpClientProvider);
    }
  }

  @Bean
  @ConditionalOnMissingBean
  public LoadBalancer kdrLoadBalancer() {
    return new RandomLoadBalancer();
  }

  @Bean
  @ConditionalOnMissingBean
  public MappingsValidator kdrMappingsValidator() {
    return new MappingsValidator();
  }

  @Bean
  @ConditionalOnMissingBean
  public RequestForwarder kdrRequestForwarder(
    HttpClientProvider httpClientProvider,
    MappingsProvider mappingsProvider,
    LoadBalancer loadBalancer,
    Optional<MeterRegistry> meterRegistry,
    ProxyingTraceInterceptor traceInterceptor,
    PostForwardResponseInterceptor responseInterceptor
  ) {
    return new RequestForwarder(
      serverProperties, kdrProperties, httpClientProvider,
      mappingsProvider, loadBalancer, meterRegistry,
      traceInterceptor, responseInterceptor);
  }

  @Bean
  @ConditionalOnMissingBean
  public TraceInterceptor kdrTraceInterceptor() {
    return new LoggingTraceInterceptor();
  }

  @Bean
  @ConditionalOnMissingBean
  public ProxyingTraceInterceptor kdrProxyingTraceInterceptor(TraceInterceptor traceInterceptor) {
    return new ProxyingTraceInterceptor(kdrProperties, traceInterceptor);
  }

  @Bean
  @ConditionalOnMissingBean
  public PreForwardRequestInterceptor kdrPreForwardRequestInterceptor(EnvConfig envConfig) {
    //return new NoOpPreForwardRequestInterceptor();
    return new AuthRequestInterceptor(kuaidaoresumePropreties.getSigningSecret(), envConfig);
  }

  @Bean
  @ConditionalOnMissingBean
  public PostForwardResponseInterceptor kdrPostForwardResponseInterceptor() {
    //return new NoOpPostForwardResponseInterceptor();
    return new CacheResponseInterceptor();
  }
}

