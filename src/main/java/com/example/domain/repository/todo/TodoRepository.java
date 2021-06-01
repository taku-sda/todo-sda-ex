package com.example.domain.repository.todo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.model.Todo;

/**
 * Todoエンティティに対応したJpaRepository.
 */
public interface TodoRepository extends JpaRepository<Todo, Integer>, TodoRepositoryCustom {

  /**
   * ユーザーに対応する今日中のToDoのリストを取得する. 期限の日付が今日と一致して、完了状態でないレコードのリスト.
   * @param userId リストを取得するユーザーのID
   * @return ToDoのリスト
   */
  @Query("SELECT t FROM Todo t WHERE t.user.userId = :userId "
      + "AND CAST(t.deadline AS date) = CURRENT_DATE AND t.completed = FALSE")
  List<Todo> getTodayList(@Param("userId") String userId);

  /**
   * ユーザーに対応する期限切れのToDoのリストを取得する. 期限が過ぎており、完了状態でないレコードのリスト
   * @param userId リストを取得するユーザーのID
   * @return ToDoのリスト
   */
  @Query("SELECT t FROM Todo t WHERE t.user.userId = :userId "
      + "AND t.deadline < CURRENT_TIMESTAMP AND t.completed = FALSE")
  List<Todo> getExpiredList(@Param("userId") String userId);

  /**
   * 指定したユーザーの完了状態のToDoを全て削除する.
   * @param userId ToDoを削除するユーザーのID
   * @return 削除した件数
   */
  @Transactional
  @Modifying
  @Query("DELETE FROM Todo t WHERE t.user.userId = :userId AND t.completed = TRUE")
  int deleteAllCompleted(@Param("userId") String userId);

  /**
   * 指定したユーザーの期限切れのToDoをすべて削除する. 期限が過ぎており、完了状態でないToDoを対象とする.
   * @param userId
   * @return 削除した件数
   */
  @Transactional
  @Modifying
  @Query("DELETE FROM Todo t WHERE t.user.userId = :userId AND t.deadline < CURRENT_TIMESTAMP "
      + "AND t.completed = FALSE")
  int deleteAllExpired(@Param("userId") String userId);

  /**
   * 指定したユーザーのToDoをすべて削除する.
   * @param userId ToDoを削除するユーザーのID
   * @return 削除した件数
   */
  @Transactional
  @Modifying
  @Query("DELETE FROM Todo t WHERE t.user.userId = :userId")
  int deleteAllOwned(@Param("userId") String userId);
}
