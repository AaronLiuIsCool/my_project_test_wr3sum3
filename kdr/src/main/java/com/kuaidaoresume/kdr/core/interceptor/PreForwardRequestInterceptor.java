package com.kuaidaoresume.kdr.core.interceptor;

import com.kuaidaoresume.kdr.config.MappingProperties;
import com.kuaidaoresume.kdr.core.http.RequestData;

public interface PreForwardRequestInterceptor {

  void intercept(RequestData data, MappingProperties mapping);
}
