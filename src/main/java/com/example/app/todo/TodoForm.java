package com.example.app.todo;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * ToDo内容の操作用のフォームクラス.
 */
@SuppressWarnings("serial")
@Getter
@Setter
public class TodoForm implements Serializable {

  /** タイトルの最大文字数. */
  private static final int TITLE_MAX_LENGTH = 20;
  /** 優先度の最小値. */
  private static final int PRIORITY_MIN_VALUE = 1;
  /** 優先度の最大値. */
  private static final int PRIORITY_MAX_VALUE = 5;
  /** メモの最大文字数. */
  private static final int MEMO_MAX_LENGTH = 400;

  /** タイトル.  */
  @NotNull(message = "タイトルが未入力です")
  @Size(max = TITLE_MAX_LENGTH, message = "文字数オーバーです")
  private String title;

  /** 期限. HTMLのdatetime-localのフォーマットに従った文字列 */
  @NotNull(message = "期限が未入力です")
  @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}",
      message = "日時の入力内容が不正です")
  private String deadlineStr;

  /** 優先度.  */
  @Min(value = PRIORITY_MIN_VALUE, message = "優先度の入力内容が不正です")
  @Max(value = PRIORITY_MAX_VALUE, message = "優先度の入力内容が不正です")
  private int priority;

  /** メモ.  */
  @Size(max = MEMO_MAX_LENGTH, message = "文字数オーバーです")
  private String memo;
}
