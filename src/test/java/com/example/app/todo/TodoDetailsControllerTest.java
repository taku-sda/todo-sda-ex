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
  class todoFormTest {

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
    @DisplayName("ログイン中のユーザーとTodoの所有者が異なる場合、例外をスロー")
    void wrongUserId() throws Exception {
      //ログインユーザーと異なるユーザーを所有者に設定
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

      mockMvc.perform(get("/todoDetails").with(user(userDetails)).param("todoId", "1"))
        .andExpect(model().attributeExists("todoForm"))
        .andExpect(model().attribute("todoId", 1))
        .andExpect(model().attribute("completed", false))
        .andExpect(model().attribute("lastUpdate", lastUpdateLdt))
        .andExpect(status().isOk())
        .andExpect(view().name("todo/todoDetails"));

      verify(service, times(1)).getTodo(1);
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
          .flashAttr("todoForm", form))
        .andExpect(model().hasErrors())
        .andExpect(status().isOk())
        .andExpect(view().name("todo/todoDetails"));
    }

    @Test
    @DisplayName("処理が正常に完了した場合、Todo一覧へリダイレクト")
    void correctTransition() throws Exception {
      mockMvc.perform(post("/todoDetails").with(csrf()).with(user(userDetails)).param("todoId", "1")
          .flashAttr("todoForm", form))
          .andExpect(status().isFound())
          .andExpect(view().name("redirect:/todoList"));

      LocalDateTime expectedDeadline = LocalDateTime.of(2000, 1, 1, 12, 00);
      verify(service, times(1)).updateDetails(1, "タイトル", expectedDeadline, 5, "メモ");
    }
  }
}
