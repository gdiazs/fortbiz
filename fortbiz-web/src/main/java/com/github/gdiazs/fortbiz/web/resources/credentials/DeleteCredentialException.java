package com.github.gdiazs.fortbiz.web.resources.credentials;

public class DeleteCredentialException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DeleteCredentialException(String message, Throwable cause) {
    super(message, cause);
  }

  public DeleteCredentialException(String message) {
    super(message);
  }
}
