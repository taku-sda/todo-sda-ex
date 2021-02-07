package com.example.domain.repository.todo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName(" TodoRepositoryImplの単体テスト")
class TodoRepositoryImplTest {

  @Autowired
  TestEntityManager entityManager;

  @Autowired
  TodoRepository repository;

  User user = new User("user", "password", RoleName.TEST);
  User wrongUser = new User("wrongUser", "password", RoleName.TEST);
  
  @BeforeEach
  void setUp() {
    entityManager.persist(user);
    entityManager.persist(wrongUser);
  }

  @Test
  @DisplayName("getNormalListAnySorted()のテスト")
  void getNormalListAnySortedTest() {
    LocalDateTime now = LocalDateTime.now();
    entityManager.persist(new Todo(null, user, "レコード1", now.plusDays(1), 1, "", false, now));
    entityManager.persist(new Todo(null, user, "レコード2", now.plusDays(2), 1, "", false, now));
    entityManager.persist(new Todo(null, user, "レコード3", now.plusDays(1), 1, "", true, now));  //完了状態
    entityManager.persist(new Todo(null, wrongUser, "レコード4", now.plusDays(1), 1, "", false, now));  //ユーザーが異なる
    entityManager.persist(new Todo(null, user, "レコード5", now.minusDays(1), 1, "", false, now));  //期限切れ
    
    List<Todo> resultList = repository.getNormalListAnySorted(user.getUserId(), "deadline", "ASC");
    assertThat(resultList.size(), is(2));
    assertThat(resultList.get(0).getTitle(), is("レコード1"));
    assertThat(resultList.get(1).getTitle(), is("レコード2"));
    
    resultList = repository.getNormalListAnySorted(user.getUserId(), "deadline", "DESC");
    assertThat(resultList.size(), is(2));
    assertThat(resultList.get(0).getTitle(), is("レコード2"));
    assertThat(resultList.get(1).getTitle(), is("レコード1"));
  }

  @Test
  @DisplayName("getCompletedListAnySorted()のテスト")
  void getCompletedListAnySortedTest() {
    LocalDateTime now = LocalDateTime.now();
    entityManager.persist(new Todo(null, user, "レコード1", now.minusDays(1), 1, "", true, now));
    entityManager.persist(new Todo(null, user, "レコード2", now.plusDays(1), 1, "", true, now));
    entityManager.persist(new Todo(null, user, "レコード3", now, 1, "", false, now));  //未完了状態
    entityManager.persist(new Todo(null, wrongUser, "レコード4", now.plusDays(1), 1, "", false, now));  //ユーザーが異なる

    List<Todo> resultList = repository.getCompletedListAnySorted(user.getUserId(), "deadline", "ASC");
    assertThat(resultList.size(), is(2));
    assertThat(resultList.get(0).getTitle(), is("レコード1"));
    assertThat(resultList.get(1).getTitle(), is("レコード2"));

    resultList = repository.getCompletedListAnySorted(user.getUserId(), "deadline", "DESC");
    assertThat(resultList.size(), is(2));
    assertThat(resultList.get(0).getTitle(), is("レコード2"));
    assertThat(resultList.get(1).getTitle(), is("レコード1"));
  }

  @Test
  @DisplayName("getExpiredListAnySorted()のテスト")
  void getExpiredListAnySortedTest() {
    LocalDateTime now = LocalDateTime.now();
    entityManager.persist(new Todo(null, user, "レコード1", now.minusDays(2), 1, "", false, now));
    entityManager.persist(new Todo(null, user, "レコード2", now.minusDays(1), 1, "", false, now));
    entityManager.persist(new Todo(null, user, "レコード3", now.plusDays(1), 1, "", false, now));  //期限切れでない
    entityManager.persist(new Todo(null, user, "レコード4", now, 1, "", true, now));  //完了状態
    entityManager.persist(new Todo(null, wrongUser, "レコード5", now.plusDays(1), 1, "", false, now));  //ユーザーが異なる

    List<Todo> resultList = repository.getExpiredListAnySorted(user.getUserId(), "deadline", "ASC");
    assertThat(resultList.size(), is(2));
    assertThat(resultList.get(0).getTitle(), is("レコード1"));
    assertThat(resultList.get(1).getTitle(), is("レコード2"));

    resultList = repository.getExpiredListAnySorted(user.getUserId(), "deadline", "DESC");
    assertThat(resultList.size(), is(2));
    assertThat(resultList.get(0).getTitle(), is("レコード2"));
    assertThat(resultList.get(1).getTitle(), is("レコード1"));
  }
}
