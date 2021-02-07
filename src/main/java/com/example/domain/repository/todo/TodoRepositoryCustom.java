package com.example.domain.repository.todo;

import java.util.List;

import com.example.domain.model.Todo;

/**
 * Todoエンティティのカスタムリポジトリクラスのインターフェース.
 */
public interface TodoRepositoryCustom {

  /**
   * ユーザーに対応する通常のToDoリストを、指定の方法でソートして取得する.
   * 期限が過ぎておらず、完了状態ではないToDoのリスト
   * @param userId リストを取得するユーザーのID
   * @param sort ソートの対象となるプロパティ名
   * @param order ソートの順序.ASCまたはDESC
   * @return ToDoのリスト
   */
  List<Todo> getNormalListAnySorted(String userId, String sort, String order);

  /**
   * ユーザーに対応する完了状態のToDoリストを、指定の方法でソートして取得する.
   * 期限が過ぎておらず、完了状態のToDoのリスト
    * @param userId リストを取得するユーザーのID
   * @param sort ソートの対象となるプロパティ名
   * @param order ソートの順序.ASCまたはDESC
   * @return ToDoのリスト
   */
  List<Todo> getCompletedListAnySorted(String userId, String sort, String order);

  /**
   * ユーザーに対応する期限切れのToDoリストを、指定の方法でソートして取得する.
   * 期限が過ぎており、未完了状態のToDoのリスト
    * @param userId リストを取得するユーザーのID
   * @param sort ソートの対象となるプロパティ名
   * @param order ソートの順序.ASCまたはDESC
   * @return ToDoのリスト
   */
  List<Todo> getExpiredListAnySorted(String userId, String sort, String order);
}
