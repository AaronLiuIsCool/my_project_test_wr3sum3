package com.kuaidaoresume.kdr.exceptions;

public class KdrException extends RuntimeException {
  public KdrException(String message) {
    super(message);
  }

  public KdrException(String message, Throwable cause) {
    super(message, cause);
  }
}
