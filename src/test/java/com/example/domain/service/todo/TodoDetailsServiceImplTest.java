package com.example.domain.service.todo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.domain.model.RoleName;
import com.example.domain.model.Todo;
import com.example.domain.model.User;
import com.example.domain.repository.todo.TodoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoDetailsServiceImplの単体テスト")
class TodoDetailsServiceImplTest {

  @Spy
  @InjectMocks
  TodoDetailsServiceImpl serviceImpl;

  @Mock
  TodoRepository repository;

  @Nested
  @DisplayName("getTodo()のテスト")
  class GetTodoTest {

    @Test
    @DisplayName("指定されたTodoが存在しない場合、例外がスローされる")
    void todoNotExist() throws Exception {
      when(repository.findById(1)).thenReturn(Optional.empty());

      assertThrows(IllegalArgumentException.class, () -> serviceImpl.getTodo(1));
      verify(repository, times(1)).findById(1);
    }

    @Test
    @DisplayName("指定されたTodoが正しく取得できる")
    void getCorrectTodo() throws Exception {
      User user = new User("userId", "password", RoleName.TEST);
      Todo expectedTodo = (new Todo(1, user, "タイトル", LocalDateTime.now(), 1, "memo", false,
          LocalDateTime.now()));
      when(repository.findById(1)).thenReturn(Optional.ofNullable(expectedTodo));

      Todo actualTodo = serviceImpl.getTodo(1);

      assertThat(actualTodo.getUser(), is(expectedTodo.getUser()));
      assertThat(actualTodo.getTitle(), is(expectedTodo.getTitle()));
      assertThat(actualTodo.getDeadline(), is(expectedTodo.getDeadline()));
      assertThat(actualTodo.getPriority(), is(expectedTodo.getPriority()));
      assertThat(actualTodo.getMemo(), is(expectedTodo.getMemo()));
      verify(repository, times(1)).findById(1);
    }

  }

  @Nested
  @DisplayName("bulkDeleteTodo()のテスト")
  class TestBulkDeleteTodo {

    @Test
    @DisplayName("削除対象にcompletedを指定した場合、正しく削除が実行される")
    void deleteCompleted() {
      when(repository.deleteAllCompleted("userId")).thenReturn(10);

      int result = serviceImpl.bulkDeleteTodo("userId", "completed");
      assertThat(result, is(10));
      verify(repository, times(1)).deleteAllCompleted("userId");
    }

    @Test
    @DisplayName("削除対象にexpiredを指定した場合、正しく削除が実行される")
    void deleteExpired() {
      when(repository.deleteAllExpired("userId")).thenReturn(10);

      int result = serviceImpl.bulkDeleteTodo("userId", "expired");
      assertThat(result, is(10));
      verify(repository, times(1)).deleteAllExpired("userId");
    }

    @Test
    @DisplayName("不正な条件が指定された場合、削除が実行されずに例外がスローされる")
    void illegalTarget() throws Exception {
      assertThrows(IllegalArgumentException.class, () -> serviceImpl.bulkDeleteTodo("userId",
          "wrong"));
      verify(repository, times(0)).deleteAllCompleted("userId");
      verify(repository, times(0)).deleteAllExpired("userId");
    }
  }

  @Nested
  @DisplayName("updateCompleted()のテスト")
  class UpdateCompleted {

    @Mock
    Todo updateTodo;

    @Test
    @DisplayName("更新後の完了状態にtrueが指定された場合、正しく更新処理が行われる")
    void statusIsTrue() {
      doReturn(updateTodo).when(serviceImpl).getTodo(1);

      serviceImpl.updateCompleted(1, "true");
      verify(updateTodo, times(1)).setCompleted(true);
      verify(updateTodo, times(1)).setLastUpdate(Mockito.any());
    }

    @Test
    @DisplayName("更新後の完了状態にfalseが指定された場合、正しく更新処理が行われる")
    void statusIsFalse() {
      doReturn(updateTodo).when(serviceImpl).getTodo(1);

      serviceImpl.updateCompleted(1, "false");
      verify(updateTodo, times(1)).setCompleted(false);
      verify(updateTodo, times(1)).setLastUpdate(Mockito.any());
    }

    @Test
    @DisplayName("更新後の完了状態の指定が不正な場合、更新されずに例外がスローされる")
    void statusIsIllegal() throws Exception {
      doReturn(updateTodo).when(serviceImpl).getTodo(1);

      assertThrows(IllegalArgumentException.class, () -> serviceImpl.updateCompleted(1, "wrong"));
      verify(updateTodo, times(0)).setCompleted(true);
      verify(updateTodo, times(0)).setCompleted(false);
      verify(updateTodo, times(0)).setLastUpdate(Mockito.any());
    }
  }
}
