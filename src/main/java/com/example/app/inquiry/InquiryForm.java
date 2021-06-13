package com.example.app.inquiry;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * お問い合わせ用のフォームクラス.
 */
@SuppressWarnings("serial")
@Getter
@Setter
public class InquiryForm implements Serializable {

  private static final int TEXT_MAX_LENGTH = 400;

  /** 分類. */
  @Pattern(regexp = "ご意見・ご要望|不具合について|その他", message = "お問い合わせの分類が不正です")
  private String type;

  /** 本文. */
  @NotNull(message = "お問い合わせの内容が入力されていません")
  @Size(max = TEXT_MAX_LENGTH, message = "文字数オーバーです")
  private String text;
}
