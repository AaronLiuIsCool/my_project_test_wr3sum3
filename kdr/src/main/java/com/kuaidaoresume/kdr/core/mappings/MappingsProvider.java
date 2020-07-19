package com.kuaidaoresume.kdr.core.mappings;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import com.kuaidaoresume.kdr.config.KdrProperties;
import com.kuaidaoresume.kdr.config.MappingProperties;
import com.kuaidaoresume.kdr.core.http.HttpClientProvider;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

public abstract class MappingsProvider {

  private static final ILogger log = SLoggerFactory.getLogger(MappingsProvider.class);

  protected final ServerProperties serverProperties;
  protected final KdrProperties kdrProperties;
  protected final MappingsValidator mappingsValidator;
  protected final HttpClientProvider httpClientProvider;
  protected List<MappingProperties> mappings;

  public MappingsProvider(
    ServerProperties serverProperties,
    KdrProperties kdrProperties,
    MappingsValidator mappingsValidator,
    HttpClientProvider httpClientProvider
  ) {
    this.serverProperties = serverProperties;
    this.kdrProperties = kdrProperties;
    this.mappingsValidator = mappingsValidator;
    this.httpClientProvider = httpClientProvider;
  }

  public MappingProperties resolveMapping(String originHost, HttpServletRequest request) {
    if (shouldUpdateMappings(request)) {
      updateMappings();
    }
    List<MappingProperties> resolvedMappings = mappings.stream()
      .filter(mapping -> originHost.toLowerCase().equals(mapping.getHost().toLowerCase()))
      .collect(Collectors.toList());
    if (isEmpty(resolvedMappings)) {
      return null;
    }
    return resolvedMappings.get(0);
  }

  @PostConstruct
  protected synchronized void updateMappings() {
    List<MappingProperties> newMappings = retrieveMappings();
    mappingsValidator.validate(newMappings);
    mappings = newMappings;
    httpClientProvider.updateHttpClients(mappings);
    log.info("Destination mappings updated", mappings);
  }

  protected abstract boolean shouldUpdateMappings(HttpServletRequest request);

  protected abstract List<MappingProperties> retrieveMappings();
}
