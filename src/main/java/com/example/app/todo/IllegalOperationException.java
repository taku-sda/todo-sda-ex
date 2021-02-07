package com.example.app.todo;

/**
 * 不正な操作が行われた場合の例外クラス.
 */
@SuppressWarnings("serial")
public class IllegalOperationException extends RuntimeException {
  public IllegalOperationException(String message) {
    super(message);
  }
}