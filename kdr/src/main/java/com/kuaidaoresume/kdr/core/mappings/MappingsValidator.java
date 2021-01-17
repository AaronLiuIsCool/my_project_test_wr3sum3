package com.kuaidaoresume.kdr.core.mappings;

import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.common.env.EnvConstant;
import com.kuaidaoresume.kdr.config.MappingProperties;
import com.kuaidaoresume.kdr.exceptions.KdrException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.springframework.util.CollectionUtils.isEmpty;

public class MappingsValidator {

  @Autowired
  private EnvConfig envConfig;

  public void validate(List<MappingProperties> mappings) {
    if (!isEmpty(mappings)) {
      mappings.forEach(this::correctMapping);
      int numberOfNames = mappings.stream()
        .map(MappingProperties::getName)
        .collect(toSet())
        .size();
      if (numberOfNames < mappings.size()) {
        throw new KdrException("Duplicated route names in mappings");
      }
      int numberOfHosts = mappings.stream()
        .map(MappingProperties::getHost)
        .collect(toSet())
        .size();
      if (numberOfHosts < mappings.size()) {
        throw new KdrException("Duplicated source hosts in mappings");
      }
      mappings.sort((mapping1, mapping2) -> mapping2.getHost().compareTo(mapping1.getHost()));
    }
  }

  protected void correctMapping(MappingProperties mapping) {
    validateName(mapping);
    validateDestinations(mapping);
    validateHost(mapping);
    validateTimeout(mapping);
  }

  protected void validateName(MappingProperties mapping) {
    if (isBlank(mapping.getName())) {
      throw new KdrException("Empty name for mapping " + mapping);
    }
  }

  protected void validateDestinations(MappingProperties mapping) {
    if (isEmpty(mapping.getDestinations())) {
      throw new KdrException("No destination hosts for mapping" + mapping);
    }
    List<String> correctedHosts = new ArrayList<>(mapping.getDestinations().size());
    mapping.getDestinations().forEach(destination -> {
      if (isBlank(destination)) {
        throw new KdrException("Empty destination for mapping " + mapping);
      }
      if (!destination.matches(".+://.+")) {
        String scheme = envConfig.getInternalApex().equals(EnvConstant.ENV_PROD) ? "https://" : "http://";
        destination = scheme + destination;
      }
      destination = removeEnd(destination, "/");
      correctedHosts.add(destination);
    });
    mapping.setDestinations(correctedHosts);
  }

  protected void validateHost(MappingProperties mapping) {
    if (isBlank(mapping.getHost())) {
      throw new KdrException("No source host for mapping " + mapping);
    }
  }

  protected void validateTimeout(MappingProperties mapping) {
    int connectTimeout = mapping.getTimeout().getConnect();
    if (connectTimeout < 0) {
      throw new KdrException("Invalid connect timeout value: " + connectTimeout);
    }
    int readTimeout = mapping.getTimeout().getRead();
    if (readTimeout < 0) {
      throw new KdrException("Invalid read timeout value: " + readTimeout);
    }
  }
}
