package com.example.domain.service.todo;

import java.time.LocalDateTime;

import com.example.domain.model.Todo;

public interface TodoDetailsService {

  /**
   * 指定したIDを持つTodoを取得する.
   * @param todoId 取得するTodoのID
   * @return 取得したTodo
   */
  Todo getTodo(Integer todoId);

  /**
   * 指定したIDを持つTodoの内容を更新する.
   * @param todoId 更新するTodoのID
   * @param title タイトル
   * @param deadline 期限
   * @param priority 優先度
   * @param memo メモ
   */
  void updateDetails(Integer todoId, String title, LocalDateTime deadline, Integer priority,
      String memo);

}
