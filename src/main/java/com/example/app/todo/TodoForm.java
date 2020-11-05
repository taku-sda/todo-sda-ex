package com.example.app.todo;

import java.io.Serializable;

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
public class TodoForm implements Serializable{

  /** タイトル. 最大20文字 */
  @NotNull(message = "タイトルが未入力です")
  @Size(max = 20, message = "タイトルは20文字以内です ")
  private String title;

  /** 期限. HTMLのdatetime-localのフォーマットに従った文字列 */
  @NotNull(message = "期限が未入力です")
  @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}",
      message = "日時の入力内容が不正です")
  private String deadlineStr;

  /** 優先度. 1～5の五段階で設定 */
  @NotNull(message = "優先度の入力内容が不正です")
  @Size(min = 1, max = 5, message ="優先度の入力内容が不正です")
  private int prioroty;

  /** メモ. 最大400文字 */
  @Size(max = 400, message ="メモは最大400文字です")
  private String memo;
}
