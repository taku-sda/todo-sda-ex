package com.example.domain.service.todo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.domain.model.RoleName;
import com.example.domain.model.Todo;
import com.example.domain.model.User;
import com.example.domain.repository.todo.TodoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoDetailsServiceImplの単体テスト")
class TodoDetailsServiceImplTest {

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
}