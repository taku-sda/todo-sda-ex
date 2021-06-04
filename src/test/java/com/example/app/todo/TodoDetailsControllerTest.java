package com.example.app.todo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.domain.model.RoleName;
import com.example.domain.model.Todo;
import com.example.domain.model.User;
import com.example.domain.service.todo.TodoDetailsService;
import com.example.domain.service.user.TodoUserDetails;
import com.example.domain.service.user.TodoUserDetailsService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoDetailsController.class)
@DisplayName("TodoDetailsControllerの単体テスト")
class TodoDetailsControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  TodoUserDetailsService userDetailsService;

  @MockBean
  TodoDetailsService service;

  User user = new User("userId", "password", RoleName.TEST);
  UserDetails userDetails = new TodoUserDetails(user);

  @Nested
  @DisplayName("todoForm()のテスト")
  class TodoFormTest {

    Todo registerdTodo = new Todo();
    LocalDateTime lastUpdateLdt = LocalDateTime.of(2000, 1, 1, 12, 00);

    @BeforeEach
    void setUp() {
      Mockito.reset(service);
      registerdTodo.setUser(user);
      registerdTodo.setTitle("タイトル");
      registerdTodo.setDeadline(LocalDateTime.now());
      registerdTodo.setPriority(1);
      registerdTodo.setMemo("メモ");
      registerdTodo.setCompleted(false);
      registerdTodo.setLastUpdate(lastUpdateLdt);
    }

    @Test
    @DisplayName("Todoの取得に失敗した場合、例外をスローしてエラーページへ遷移")
    void failureGetTodo() throws Exception {
      when(service.getTodo(1)).thenThrow(new IllegalArgumentException("エラーメッセージ"));

      MvcResult result = mockMvc.perform(get("/todoDetails").with(user(userDetails)).param("todoId",
          "1")).andExpect(view().name("error/error")).andReturn();

      assertThat(result.getResolvedException().getClass(), is(IllegalOperationException.class));
      verify(service, times(1)).getTodo(1);
    }

    @Test
    @DisplayName("ログイン中のユーザーとTodoの所有者が異なる場合、例外をスロー")
    void wrongUserId() throws Exception {
      // ログインユーザーと異なるユーザーを所有者に設定
      User anotherUser = new User("anotherUserId", "password", RoleName.TEST);
      registerdTodo.setUser(anotherUser);

      when(service.getTodo(1)).thenReturn(registerdTodo);

      MvcResult result = mockMvc.perform(get("/todoDetails").with(user(userDetails)).param("todoId",
          "1")).andReturn();

      assertThat(result.getResolvedException().getClass(), is(IllegalOperationException.class));
      verify(service, times(1)).getTodo(1);
    }

    @Test
    @DisplayName("モデルに表示用データを格納してTodo詳細画面へ遷移する")
    void correctTransition() throws Exception {
      when(service.getTodo(1)).thenReturn(registerdTodo);

      mockMvc.perform(get("/todoDetails").with(user(userDetails)).param("todoId", "1")).andExpect(
          model().attributeExists("todoForm")).andExpect(model().attribute("todoId", 1)).andExpect(
              model().attribute("completed", false)).andExpect(model().attribute("lastUpdate",
                  lastUpdateLdt)).andExpect(status().isOk()).andExpect(view().name(
                      "todo/todoDetails"));

      verify(service, times(1)).getTodo(1);
    }
  }

  @Nested
  @DisplayName("deleteTodo()のテスト")
  class DeleteTodoTest {

    @BeforeEach
    void setUp() {
      Mockito.reset(service);
    }

    @Test
    @DisplayName("Todoの取得に失敗した場合、例外をスローしてエラーページへ遷移")
    void failureGetTodo() throws Exception {
      when(service.getTodo(1)).thenThrow(new IllegalArgumentException("エラーメッセージ"));

      MvcResult result = mockMvc.perform(get("/todoDetails/delete").with(user(userDetails)).param(
          "todoId", "1")).andExpect(view().name("error/error")).andReturn();

      assertThat(result.getResolvedException().getClass(), is(IllegalOperationException.class));
      verify(service, times(1)).getTodo(1);
    }

    @Test
    @DisplayName("ログイン中のユーザーとTodoの所有者が異なる場合、例外をスローしてエラーページへ遷移")
    void wrongUserId() throws Exception {
      // ログインユーザーと異なるユーザーをToDoの所有者に設定
      Todo registerdTodo = new Todo();
      User anotherUser = new User("anotherUserId", "password", RoleName.TEST);
      registerdTodo.setUser(anotherUser);

      when(service.getTodo(1)).thenReturn(registerdTodo);

      MvcResult result = mockMvc.perform(get("/todoDetails/delete").with(user(userDetails)).param(
          "todoId", "1")).andExpect(view().name("error/error")).andReturn();

      assertThat(result.getResolvedException().getClass(), is(IllegalOperationException.class));
      verify(service, times(1)).getTodo(1);
    }

    @Test
    @DisplayName("削除を実行してToDo一覧画面にリダイレクトする")
    void completeDelete() throws Exception {
      // ログインユーザーをToDoの所有者に設定
      Todo registerdTodo = new Todo();
      registerdTodo.setUser(user);

      when(service.getTodo(1)).thenReturn(registerdTodo);

      mockMvc.perform(get("/todoDetails/delete").with(user(userDetails)).param("todoId", "1"))
          .andExpect(view().name("redirect:/todoList"));

      verify(service, times(1)).getTodo(1);
      verify(service, times(1)).deleteTodo(1);
    }
  }

  @Nested
  @DisplayName("blukDeleteTodo")
  class BulkDeleteTodoTest {

    @Test
    @DisplayName("一括削除に失敗した場合、例外をスローしてエラーページへ遷移")
    void failureBulkDelete() throws Exception {
      when(service.bulkDeleteTodo("userId", "wrongTarget")).thenThrow(new IllegalArgumentException(
          "エラーメッセージ"));

      MvcResult result = mockMvc.perform(get("/todoDetails/bulkDelete").with(user(userDetails))
          .param("target", "wrongTarget")).andExpect(view().name("error/error")).andReturn();

      assertThat(result.getResolvedException().getClass(), is(IllegalOperationException.class));
      verify(service, times(1)).bulkDeleteTodo("userId", "wrongTarget");
    }

    @Test
    @DisplayName("削除件数を格納して、ToDo一覧画面へリダイレクト")
    void completeBulkDelete() throws Exception {
      when(service.bulkDeleteTodo("userId", "completed")).thenReturn(10);

      mockMvc.perform(get("/todoDetails/bulkDelete").with(user(userDetails)).param("target",
          "completed")).andExpect(view().name("redirect:/todoList")).andExpect(flash().attribute(
              "deletedCount", 10));

      verify(service, times(1)).bulkDeleteTodo("userId", "completed");
    }
  }

  @Nested
  @DisplayName("updateCoompleted()のテスト")
  class UpdateCompletedTest {

    @BeforeEach
    void setUp() {
      Mockito.reset(service);
    }

    @Test
    @DisplayName("Todoの取得に失敗した場合、例外をスローしてエラーページへ遷移")
    void failureGetTodo() throws Exception {
      when(service.getTodo(1)).thenThrow(new IllegalArgumentException("エラーメッセージ"));

      MvcResult result = mockMvc.perform(get("/todoDetails/completed").with(user(userDetails))
          .param("todoId", "1").param("status", "true").param("path", "pathStr")).andExpect(view()
              .name("error/error")).andReturn();

      assertThat(result.getResolvedException().getClass(), is(IllegalOperationException.class));
      verify(service, times(1)).getTodo(1);
    }

    @Test
    @DisplayName("ログイン中のユーザーとTodoの所有者が異なる場合、例外をスローしてエラーページへ遷移")
    void wrongUserId() throws Exception {
      // ログインユーザーと異なるユーザーをToDoの所有者に設定
      Todo registerdTodo = new Todo();
      User anotherUser = new User("anotherUserId", "password", RoleName.TEST);
      registerdTodo.setUser(anotherUser);

      when(service.getTodo(1)).thenReturn(registerdTodo);

      MvcResult result = mockMvc.perform(get("/todoDetails/completed").with(user(userDetails))
          .param("todoId", "1").param("status", "true").param("path", "pathStr")).andExpect(view()
              .name("error/error")).andReturn();

      assertThat(result.getResolvedException().getClass(), is(IllegalOperationException.class));
      verify(service, times(1)).getTodo(1);
    }

    @Test
    @DisplayName("更新を実行して、遷移前のページにリダイレクトする")
    void completeUpdate() throws Exception {
      // ログインユーザーをToDoの所有者に設定
      Todo registerdTodo = new Todo();
      registerdTodo.setUser(user);

      when(service.getTodo(1)).thenReturn(registerdTodo);

      mockMvc.perform(get("/todoDetails/completed").with(user(userDetails)).param("todoId", "1")
          .param("status", "true").param("path", "pathStr")).andExpect(view().name("redirect:"
              + "pathStr"));

      verify(service, times(1)).getTodo(1);
      verify(service, times(1)).updateCompleted(1, "true");
    }

  }

  @Nested
  @DisplayName("updateDetails()のテスト")
  class UpdateDetailsTest {

    TodoForm form = new TodoForm();

    @BeforeEach
    void setUp() {
      Mockito.reset(service);
      form.setTitle("タイトル");
      form.setDeadlineStr("2000-01-01T12:00");
      form.setPriority(5);
      form.setMemo("メモ");
    }

    @Test
    @DisplayName("バリデーションエラーが存在する場合、詳細画面に遷移")
    void wrongFormInputs() throws Exception {
      form.setDeadlineStr("2000/01/01-12:00"); // 期限文字列が指定したフォーマットではない

      mockMvc.perform(post("/todoDetails").with(csrf()).with(user(userDetails)).param("todoId", "1")
          .flashAttr("todoForm", form)).andExpect(model().hasErrors()).andExpect(status().isOk())
          .andExpect(view().name("todo/todoDetails"));
    }

    @Test
    @DisplayName("処理が正常に完了した場合、Todo一覧へリダイレクト")
    void correctTransition() throws Exception {
      mockMvc.perform(post("/todoDetails").with(csrf()).with(user(userDetails)).param("todoId", "1")
          .flashAttr("todoForm", form)).andExpect(status().isFound()).andExpect(view().name(
              "redirect:/todoList"));

      LocalDateTime expectedDeadline = LocalDateTime.of(2000, 1, 1, 12, 00);
      verify(service, times(1)).updateDetails(1, "タイトル", expectedDeadline, 5, "メモ");
    }
  }

}
