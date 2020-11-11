package com.example.app.todo;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.domain.model.RoleName;
import com.example.domain.model.Todo;
import com.example.domain.model.User;
import com.example.domain.service.todo.AddTodoService;
import com.example.domain.service.user.TodoUserDetails;
import com.example.domain.service.user.TodoUserDetailsService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AddTodoController.class)
@DisplayName("AddTodoControllerの単体テスト")
class AddTodoControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  TodoUserDetailsService  userDetailsService;

  @MockBean
  AddTodoService service;

  @Nested
  @WithMockUser(username = "user", roles ="TEST")
  @DisplayName("addTodoForm()のテスト")
  class TestAddTodoForm {
  @Test
  @DisplayName("TodoFormがモデルに格納され、追加フォームに遷移する")
  void correctTransition() throws Exception {
    mockMvc.perform(get("/addTodo"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("todoForm"))
        .andExpect(view().name("todo/addTodo"));
    }
  }

  @Nested
  @DisplayName("add()のテスト")
  class TestAdd {
    UserDetails user = new TodoUserDetails(new User("user", "password", RoleName.TEST));
    TodoForm form = new TodoForm();

    @BeforeEach
    void setUp() {
      form.setTitle("タイトル");
      form.setDeadlineStr("2000-01-01T12:00");
      form.setPriority(5);
      form.setMemo("メモテキスト");
    }

    @Test
    @DisplayName("バリデーションエラーがある場合、追加フォームに遷移する")
    void wrongFormInputs() throws Exception {
      form.setDeadlineStr("2000/01/01-12:00"); //期限文字列が指定したフォーマットではない

      mockMvc.perform((post("/addTodo").with(csrf()).with(user(user)))
          .flashAttr("todoForm", form))
        .andExpect(status().isOk())
        .andExpect(model().hasErrors())
        .andExpect(view().name("todo/addTodo"));
    }

    @Test
    @DisplayName("処理が正常に完了した場合、Todo一覧画面に遷移")
    void successAdd() throws Exception {
      mockMvc.perform((post("/addTodo").with(csrf()).with(user(user)))
          .flashAttr("todoForm", form))
        .andExpect(status().isOk())
        .andExpect(view().name("todo/todoList"));

      verify(service, times(1)).add((Todo) Mockito.any());
    }
  }
}
