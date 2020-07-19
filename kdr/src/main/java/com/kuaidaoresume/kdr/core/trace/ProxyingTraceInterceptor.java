package com.kuaidaoresume.kdr.core.trace;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import com.kuaidaoresume.kdr.config.KdrProperties;

import static java.util.UUID.randomUUID;

public class ProxyingTraceInterceptor {

  protected final KdrProperties kdrProperties;
  protected final TraceInterceptor traceInterceptor;

  public ProxyingTraceInterceptor(KdrProperties kdrProperties, TraceInterceptor traceInterceptor) {
    this.kdrProperties = kdrProperties;
    this.traceInterceptor = traceInterceptor;
  }

  public String generateTraceId() { return kdrProperties.getTracing().isEnabled() ? randomUUID().toString() : null;}

  public void onRequestReceived(String traceId, HttpMethod method, String host, String uri, HttpHeaders headers) {
    runIfTracingIsEnabled(() -> {
      IncomingRequest request = getIncomingRequest(method, host, uri, headers);
      traceInterceptor.onRequestReceived(traceId, request);
    });
  }

  private IncomingRequest getIncomingRequest(HttpMethod method, String host, String uri, HttpHeaders headers) {
    IncomingRequest request = new IncomingRequest();
    request.setMethod(method);
    request.setHost(host);
    request.setUri(uri);
    request.setHeaders(headers);
    return request;
  }

  public void onNoMappingFound(String traceId, HttpMethod method, String host, String uri, HttpHeaders headers) {
    runIfTracingIsEnabled(() -> {
      IncomingRequest request = getIncomingRequest(method, host, uri, headers);
      traceInterceptor.onNoMappingFound(traceId, request);
    });
  }

  public void onForwardStart(String traceId, String mappingName, HttpMethod method, String host, String uri, byte[] body, HttpHeaders headers) {
    runIfTracingIsEnabled(() -> {
      ForwardRequest request = new ForwardRequest();
      request.setMappingName(mappingName);
      request.setMethod(method);
      request.setHost(host);
      request.setUri(uri);
      request.setBody(body);
      request.setHeaders(headers);
      traceInterceptor.onForwardStart(traceId, request);
    });
  }

  public void onForwardFailed(String traceId, Throwable error) {
    runIfTracingIsEnabled(() -> traceInterceptor.onForwardError(traceId, error));
  }

  public void onForwardComplete(String traceId, HttpStatus status, byte[] body, HttpHeaders headers) {
    runIfTracingIsEnabled(() -> {
      ReceivedResponse response = new ReceivedResponse();
      response.setStatus(status);
      response.setBody(body);
      response.setHeaders(headers);
      traceInterceptor.onForwardComplete(traceId, response);
    });
  }

  protected void runIfTracingIsEnabled(Runnable operation) {
    if (kdrProperties.getTracing().isEnabled()) {
      operation.run();
    }
  }
}

