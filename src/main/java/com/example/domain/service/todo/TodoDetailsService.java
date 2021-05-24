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

  /**
   * 指定したIDを持つTodoを削除する.
   * @param todoId 削除するTodoのID
   */
  void deleteTodo(Integer todoId);

  /**
   * 対象のユーザーのTodoを条件によって一括削除する.
   * @param userId 対象のユーザーのID
   * @param target 一括削除の対象. completed(完了済)またはexpired(期限切れ)
   * @return 削除した件数
   */
  int bulkDeleteTodo(String userId, String target);

  /**
   * 指定したIDを持つTodoの完了状態を更新する.
   * @param todoId 更新するTodoのID
   * @param status 更新後の完了状態
   */
  void updateCompleted(Integer todoId, String status);
}
