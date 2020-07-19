package com.kuaidaoresume.kdr.core.interceptor;

import com.kuaidaoresume.kdr.config.MappingProperties;
import com.kuaidaoresume.kdr.core.http.ResponseData;

public interface PostForwardResponseInterceptor {

  void intercept(ResponseData data, MappingProperties mapping);
}
