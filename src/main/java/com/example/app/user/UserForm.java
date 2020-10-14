package com.example.app.user;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

/**
 * ユーザー登録操作用のフォームクラス.
 */
@SuppressWarnings("serial")
@Getter
@Setter
public class UserForm implements Serializable {

  /** ユーザーID. 英数字10文字以内*/
  @NotNull(message = "ユーザーIDが未入力です")
  @Pattern(regexp = "[a-zA-Z0-9]{1,10}", message = "ユーザーIDは英数字10文字以内です")
  private String userId;

  /** パスワード. 英数字4～8文字 */
  @NotNull(message = "パスワードが未入力です")
  @Pattern(regexp = "[a-zA-Z0-9]{4,8}", message = "パスワードは英数字4～8文字です")
  private String password;
}
