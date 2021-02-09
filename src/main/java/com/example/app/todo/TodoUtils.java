package com.example.app.todo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ToDo関連の共通処理をまとめたクラス
 */
public class TodoUtils {

  /**
   * 日時を表す文字列をLocalDateTimeに変換する.
   * @param deadlineStr 日時を表す文字列
   * @return 変換後のLocalDateTime
   */
  public static LocalDateTime convertToLocalDateTime(String deadlineStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    return LocalDateTime.parse(deadlineStr, formatter);
  }

}
