package com.lamine.book.exception;

public class OperationNotPermittedException extends RuntimeException {
  public OperationNotPermittedException(String s) {
    super(s);
  }
}
