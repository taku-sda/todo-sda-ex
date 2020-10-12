package com.example.domain.service.user;

import com.example.domain.model.User;

public interface UserRegisterService {
  /**
   * ユーザーIDが重複せずに使用可能か確認する.
   * 重複している場合は、例外をスローする
   * @param userId  確認するユーザーID
   * @throws ExistUserException ユーザーIDが重複している場合
   */
  void confirmAvailability(String userId) ;
  
  /**
   * ユーザー情報の登録処理を行う.
   * パスワードはBCryptでハッシュ化を行って登録する
   * @param registerUser  登録するユーザー
   * @return  登録されたユーザー
   */
  User register(User registerUser);
}
