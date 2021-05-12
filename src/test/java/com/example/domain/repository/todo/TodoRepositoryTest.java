package com.example.domain.repository.todo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.domain.model.RoleName;
import com.example.domain.model.Todo;
import com.example.domain.model.User;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DisplayName("TodoRepositoryの単体テスト")
class TodoRepositoryTest {

  @Autowired
  TestEntityManager entityManager;

  @Autowired
  TodoRepository repository;

  User user = new User("user", "password", RoleName.TEST); // 正しいユーザー情報
  User wrongUser = new User("wrongUser", "password", RoleName.TEST); // 誤っているユーザー情報

  @BeforeEach
  void setUp() {
    entityManager.persist(user);
    entityManager.persist(wrongUser);
  }

  @Nested
  @DataJpaTest
  @DisplayName("リスト取得処理のテスト")
  class testGetList {

    @Test
    @DisplayName("getTodayList()のテスト")
    void getTodayListTest() {
      LocalDateTime now = LocalDateTime.now();
      entityManager.persist(new Todo(null, user, "レコード1", now, 1, "", false, now));
      entityManager.persist(new Todo(null, wrongUser, "レコード2", now, 1, "", false, now)); // ユーザーが異なる
      entityManager.persist(new Todo(null, user, "レコード3", now, 1, "", true, now)); // 完了状態
      entityManager.persist(new Todo(null, user, "レコード4", now.plusDays(1), 1, "", false, now)); // 期限が翌日
      entityManager.persist(new Todo(null, user, "レコード5", now.minusDays(1), 1, "", false, now)); // 期限が前日

      List<Todo> resultList = repository.getTodayList(user.getUserId());
      assertThat(resultList.size(), is(1));
      assertThat(resultList.get(0).getTitle(), is("レコード1"));
    }

    @Test
    @DisplayName("getExpiredList()のテスト")
    void getExpiredListTest() {
      LocalDateTime now = LocalDateTime.now();
      entityManager.persist(new Todo(null, user, "レコード1", now.minusDays(1), 1, "", false, now));
      entityManager.persist(new Todo(null, wrongUser, "レコード2", now, 1, "", false, now)); // ユーザーが異なる
      entityManager.persist(new Todo(null, user, "レコード3", now, 1, "", true, now)); // 完了状態
      entityManager.persist(new Todo(null, user, "レコード4", now.plusDays(1), 1, "", false, now)); // 期限を過ぎていない

      List<Todo> resultList = repository.getExpiredList(user.getUserId());
      assertThat(resultList.size(), is(1));
      assertThat(resultList.get(0).getTitle(), is("レコード1"));
    }
  }

  @Nested
  @DataJpaTest
  @DisplayName("一括削除処理のテスト")
  class testMultiDelete {

    @BeforeEach
    void setUp() {
      LocalDateTime now = LocalDateTime.now();
      entityManager.persist((new Todo(null, user, "正ユーザー期限内未完了", now.plusDays(1), 1, "", false,
          now)));
      entityManager.persist((new Todo(null, wrongUser, "誤ユーザー期限内未完了", now.plusDays(1), 1, "", false,
          now)));
      entityManager.persist((new Todo(null, user, "正ユーザー期限内完了済", now.plusDays(1), 1, "", true,
          now)));
      entityManager.persist((new Todo(null, wrongUser, "誤ユーザー期限内完了済", now.plusDays(1), 1, "", true,
          now)));
      entityManager.persist((new Todo(null, user, "正ユーザー期限切れ未完了", now.minusDays(1), 1, "", false,
          now)));
      entityManager.persist((new Todo(null, wrongUser, "誤ユーザー期限切れ未完了", now.minusDays(1), 1, "",
          false, now)));
      entityManager.persist((new Todo(null, user, "正ユーザー期限切れ完了済", now.minusDays(1), 1, "", true,
          now)));
      entityManager.persist((new Todo(null, wrongUser, "誤ユーザー期限切れ完了済", now.minusDays(1), 1, "",
          true, now)));
    }

    @Test
    @DisplayName("deleteAllCompleted()のテスト")
    void deleteAllCompletedTest() {
      int result = repository.deleteAllCompleted(user.getUserId());
      assertThat(result, is(2));
    }

    @Test
    @DisplayName("deleteAllExpired()のテスト")
    void deleteAllExpiredTest() {
      int result = repository.deleteAllExpired(user.getUserId());
      assertThat(result, is(1));
    }
  }
}
