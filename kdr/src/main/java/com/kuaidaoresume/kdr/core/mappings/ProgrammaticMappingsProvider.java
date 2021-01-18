package com.kuaidaoresume.kdr.core.mappings;

import com.kuaidaoresume.common.env.EnvConstant;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.common.services.Service;
import com.kuaidaoresume.common.services.ServiceDirectory;
import com.kuaidaoresume.kdr.config.KdrProperties;
import com.kuaidaoresume.kdr.config.MappingProperties;
import com.kuaidaoresume.kdr.core.http.HttpClientProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProgrammaticMappingsProvider extends MappingsProvider {
  protected final EnvConfig envConfig;

  public ProgrammaticMappingsProvider(
    EnvConfig envConfig,
    ServerProperties serverProperties,
    KdrProperties kdrProperties,
    MappingsValidator mappingsValidator,
    HttpClientProvider httpClientProvider
  ) {
    super(serverProperties, kdrProperties, mappingsValidator, httpClientProvider);
    this.envConfig = envConfig;
  }

  @Override
  protected boolean shouldUpdateMappings(HttpServletRequest request) {
    //return false;
    return true;
  }

  @Override
  protected List<MappingProperties> retrieveMappings() {
    List<MappingProperties> mappings = new ArrayList<>();
    Map<String, Service> serviceMap = ServiceDirectory.getMapping();
    for(String key : serviceMap.keySet()) {
      String subDomain = key.toLowerCase();
      Service service = serviceMap.get(key);
      MappingProperties mapping = new MappingProperties();
      mapping.setName(subDomain + "_route");
      mapping.setHost(subDomain + "." + envConfig.getExternalApex());
      // No security on backend right now TODO Aaron Liu
      String dest = "http://" + service.getBackendDomain();
      mapping.setDestinations(Arrays.asList(dest));
      mappings.add(mapping);
    }
    return mappings;
  }
}

