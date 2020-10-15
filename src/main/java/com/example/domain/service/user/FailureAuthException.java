package com.example.domain.service.user;

/**
 * HttpServletRequestのメソッドを用いた認証処理に失敗した場合の例外クラス.
 */
@SuppressWarnings("serial")
public class FailureAuthException extends RuntimeException {
  public FailureAuthException(String message) {
    super(message);
  }
}
