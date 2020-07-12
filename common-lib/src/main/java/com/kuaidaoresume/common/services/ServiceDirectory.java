package com.kuaidaoresume.common.services;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * ServiceDirectory allows access to a backend service using its subdomain
 *
 * @auther Aaron Liu
 * Kuaidaoresume Services ia a map of subdomains -> specs
 * Subdomain is <string> + Env["rootDomain"]
 * e.g. "login" service on prod is "login" + "kuaidao.com"
 *
 * PLEASE KEEP THIS LIST IN ALPHABETICAL ORDER
 */
public class ServiceDirectory {

  private static Map<String, Service> serviceMap;

  static {
    Map<String, Service> map = new TreeMap<>();

    Service service = Service.builder()
      .security(SecurityConstant.SEC_AUTHENTICATED)
      .restrictDev(false)
      .backendDomain("account-service")
      .build();
    map.put("account", service);

    service = Service.builder()
      // Debug site for kdr proxy
      .security(SecurityConstant.SEC_PUBLIC)
      .restrictDev(true)
      .backendDomain("httpbin.org")
      .build();
    map.put("kdr", service);

    serviceMap = Collections.unmodifiableMap(map);
  }

  public static Map<String, Service> getMapping() {
    return serviceMap;
  }
}
