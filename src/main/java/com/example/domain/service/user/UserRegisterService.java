package com.example.domain.service.user;

import javax.servlet.http.HttpServletRequest;

import com.example.domain.model.User;

public interface UserRegisterService {
  /**
   * ユーザーIDが重複せずに使用可能か確認する.
   * 重複している場合は例外をスローする
   * @param userId 確認するユーザーID
   * @throws ExistUserException ユーザーIDが重複している場合
   */
  void confirmAvailability(String userId);

  /**
   * ユーザー情報の登録処理を行う.
   * パスワードはBCryptでハッシュ化を行って登録する
   * @param registerUser 登録するユーザー
   * @return 登録されたユーザー
   */
  User register(User registerUser);

  /**
   * ユーザーのログイン認証を行う.
   * フォーム認証ではなく、HttpServletRequestにより直接認証を行う
   * @param request  認証を行うHttpServletRequestオブジェクト
   * @param username 認証に使用するユーザーID
   * @param password 認証に使用するパスワード
   * @throws FailureLoginException 認証に失敗した場合
   */
  void authWithHttpServletRequest(HttpServletRequest request, String username, String password);
}
