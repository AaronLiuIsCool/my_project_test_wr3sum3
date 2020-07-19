package com.kuaidaoresume.kdr.core.mappings;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import com.kuaidaoresume.kdr.config.KdrProperties;
import com.kuaidaoresume.kdr.config.MappingProperties;
import com.kuaidaoresume.kdr.core.http.HttpClientProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationMappingsProvider extends MappingsProvider {

  public ConfigurationMappingsProvider(
    ServerProperties serverProperties,
    KdrProperties kdrProperties,
    MappingsValidator mappingsValidator,
    HttpClientProvider httpClientProvider
  ) {
    super(serverProperties, kdrProperties,
      mappingsValidator, httpClientProvider);
  }


  @Override
  protected boolean shouldUpdateMappings(HttpServletRequest request) {
    return false;
  }

  @Override
  protected List<MappingProperties> retrieveMappings() {
    return kdrProperties.getMappings().stream()
      .map(MappingProperties::copy)
      .collect(Collectors.toList());
  }
}
