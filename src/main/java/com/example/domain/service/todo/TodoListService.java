package com.example.domain.service.todo;

import java.util.List;

import com.example.domain.model.Todo;

/**
 * ToDoの一覧表示に関する処理を行うインターフェース.
 */
public interface TodoListService {

  /**
   * 表示用のToDo一覧のリストを取得する. リストの種類、ソートの対象、ソートの順序を指定することができる.
   * @param userId リストを取得するユーザーのID
   * @param listType リストの種類.normal(通常)、completed(完了状態)、expired(期限切れ)の3種類
   * @param sort ソートの対象となるプロパティ名.deadline(期限)、priority(優先度)、lastUpdate(最終更新日時)の3種類
   * @param order ソートの順序.ASCまたはDESC
   * @return 表示用のToDo一覧のリスト
   */
  List<Todo> getDisplayList(String userId, String listType, String sort, String order);

  /**
   * 当日中のToDoの件数を取得する.
   * @param userId 件数を取得するユーザーのID
   * @return ToDoの件数
   */
  int todayListSize(String userId);

  /**
   * 期限切れのToDoの件数を取得する.
   * @param userId 件数を取得するユーザーのID
   * @return ToDoの件数
   */
  int expiredListSize(String userId);
}
