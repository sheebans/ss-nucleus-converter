package org.gooru.converter.processors.exceptions;

public class NotFoundException extends RuntimeException {

  private static final long serialVersionUID = 8928901602275508377L;

  public NotFoundException() {
  }

  public NotFoundException(String message) {
    super(message);
  }
  

  public NotFoundException(Throwable cause) {
    super(cause);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
