package com.example.domain.service.user;

/**
 * 登録するユーザーIDが重複している例外クラス.
 */
@SuppressWarnings("serial")
public class ExistUserException extends RuntimeException {
  public ExistUserException(String message) {
    super(message);
  }
}