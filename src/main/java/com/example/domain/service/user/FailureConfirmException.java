package com.example.domain.service.user;

/**
 * ユーザー情報の確認に失敗した場合の例外クラス.
 */
@SuppressWarnings("serial")
public class FailureConfirmException extends RuntimeException {
  public FailureConfirmException(String message) {
    super(message);
  }
}
