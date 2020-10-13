package com.example.domain.service.user;

/**
 * ユーザー登録後の自動ログインに失敗した場合の例外クラス.
 */
@SuppressWarnings("serial")
public class FailureLoginException extends RuntimeException {
  public FailureLoginException(String message) {
    super(message);
  }

}
