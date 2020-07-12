package com.kuaidaoresume.kdr.core.interceptor;

import org.springframework.http.HttpHeaders;
import com.kuaidaoresume.kdr.config.MappingProperties;
import com.kuaidaoresume.kdr.core.http.ResponseData;

import java.util.List;

public class CacheResponseInterceptor implements PostForwardResponseInterceptor {
  @Override
  public void intercept(ResponseData data, MappingProperties mapping) {
    HttpHeaders respHeaders = data.getHeaders();
    if (respHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
      List<String> values = respHeaders.get(HttpHeaders.CONTENT_TYPE);
      if (values.contains("text/html")) {
        // insert header to prevent caching
        respHeaders.set(HttpHeaders.CACHE_CONTROL, "no-cache");
      }
    }
  }
}
