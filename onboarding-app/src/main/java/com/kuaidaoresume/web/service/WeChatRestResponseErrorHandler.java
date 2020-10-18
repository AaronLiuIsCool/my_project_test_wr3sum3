package com.kuaidaoresume.web.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class WeChatRestResponseErrorHandler implements ResponseErrorHandler {

    static final ILogger logger = SLoggerFactory.getLogger(WeChatRestResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is4xxClientError() ||
                httpResponse.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
         if (httpResponse.getStatusCode().is4xxClientError()) {
             // TODO: handle WeChat client error
             logger.error(httpResponse.getStatusText());
         }

         if (httpResponse.getStatusCode().is5xxServerError()) {
             // TODO: handle WeChat server error
             logger.error(httpResponse.getStatusText());
         }
    }
}
