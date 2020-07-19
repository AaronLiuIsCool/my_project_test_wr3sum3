package com.kuaidaoresume.kdr.core.http;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static com.kuaidaoresume.kdr.core.utils.BodyConverter.convertBodyToString;
import static com.kuaidaoresume.kdr.core.utils.BodyConverter.convertStringToBody;

@Getter
@Setter
public class ResponseData {


  protected HttpStatus status;
  protected HttpHeaders headers;
  protected byte[] body;

  @Setter(AccessLevel.NONE)
  protected UnmodifiableRequestData requestData;

  public ResponseData(HttpStatus status, HttpHeaders headers, byte[] body, UnmodifiableRequestData requestData) {
    this.status = status;
    this.headers = new HttpHeaders();
    this.headers.putAll(headers);
    this.body = body;
    this.requestData = requestData;
  }
}

