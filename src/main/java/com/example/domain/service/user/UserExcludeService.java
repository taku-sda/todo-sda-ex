package com.example.domain.service.user;

import javax.servlet.http.HttpServletRequest;

public interface UserExcludeService {

  /**
   * ユーザーIDに対して正しいパスワードか確認する.
   * @param userId  対象のユーザーID
   * @param password 確認するパスワード
   */
  void confirmPassword(String userId, String password);

  /**
   * ユーザー情報の削除処理を行う.
   * @param userId  削除するユーザー
   */
  void exclude(String userId);

  /**
   * ユーザーのログアウトを行う.
   * @param request ログアウトするHttpServletRequestオブジェクト
   */
  void logoutWithHttpServletRequest(HttpServletRequest request);
}
