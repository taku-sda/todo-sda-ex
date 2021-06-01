package com.example.domain.service.todo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.domain.model.RoleName;
import com.example.domain.model.Todo;
import com.example.domain.model.User;
import com.example.domain.repository.todo.TodoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoListServiceImplの単体テスト")
class TodoListServiceImplTest {

  User user = new User("userId", "password", RoleName.TEST);

  @InjectMocks
  TodoListServiceImpl serviceImpl;

  @Mock
  TodoRepository repository;

  @Nested
  @DisplayName("getDisplayList()のテスト")
  class GetDisplayListTest {

    @ParameterizedTest
    @DisplayName("一覧の取得条件が不正の場合、例外がスローされる")
    @CsvSource({ "'wrong', 'deadline', 'ASC'", // listTypeが不正
        "'normal', 'wrong', 'ASC'", // sortが不正
        "'wrong', 'deadline', 'wrong'" // orderが不正
    })
    void wrongAuguments(String listType, String sort, String order) {
      assertThrows(IllegalArgumentException.class, () -> serviceImpl.getDisplayList(user
          .getUserId(), listType, sort, order));
    }

    @Test
    @DisplayName("listTypeがnormalの時、対応するメソッドからリストを取得できる")
    void listTypeNormal() {
      List<Todo> returnList = new ArrayList<>();
      returnList.add(new Todo(1, user, "タイトル", LocalDateTime.now(), 1, "memo", false, LocalDateTime
          .now()));
      when(repository.getNormalListAnySorted(user.getUserId(), "deadline", "ASC")).thenReturn(
          returnList);

      List<Todo> resultList = serviceImpl.getDisplayList(user.getUserId(), "normal", "deadline",
          "ASC");
      assertThat(resultList.size(), is(1));
      assertThat(resultList.get(0).getTitle(), is("タイトル"));
      verify(repository, times(1)).getNormalListAnySorted(user.getUserId(), "deadline", "ASC");
    }

    @Test
    @DisplayName("listTypeがcompletedの時、対応するメソッドからリストを取得できる")
    void listTypeCompleted() {
      List<Todo> returnList = new ArrayList<>();
      returnList.add(new Todo(1, user, "タイトル", LocalDateTime.now(), 1, "memo", false, LocalDateTime
          .now()));
      when(repository.getCompletedListAnySorted(user.getUserId(), "deadline", "ASC")).thenReturn(
          returnList);

      List<Todo> resultList = serviceImpl.getDisplayList(user.getUserId(), "completed", "deadline",
          "ASC");
      assertThat(resultList.size(), is(1));
      assertThat(resultList.get(0).getTitle(), is("タイトル"));
      verify(repository, times(1)).getCompletedListAnySorted(user.getUserId(), "deadline", "ASC");
    }

    @Test
    @DisplayName("listTypeがexpiredの時、対応するメソッドからリストを取得できる")
    void listTypeExpired() {
      List<Todo> returnList = new ArrayList<>();
      returnList.add(new Todo(1, user, "タイトル", LocalDateTime.now(), 1, "memo", false, LocalDateTime
          .now()));
      when(repository.getExpiredListAnySorted(user.getUserId(), "deadline", "ASC")).thenReturn(
          returnList);

      List<Todo> resultList = serviceImpl.getDisplayList(user.getUserId(), "expired", "deadline",
          "ASC");
      assertThat(resultList.size(), is(1));
      assertThat(resultList.get(0).getTitle(), is("タイトル"));
      verify(repository, times(1)).getExpiredListAnySorted(user.getUserId(), "deadline", "ASC");
    }
  }
}
