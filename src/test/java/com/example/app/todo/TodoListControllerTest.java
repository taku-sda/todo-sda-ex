package com.example.app.todo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.example.domain.service.todo.TodoListService;
import com.example.domain.service.user.TodoUserDetails;
import com.example.domain.service.user.TodoUserDetailsService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoListController.class)
@DisplayName("TodoListControllerの単体テスト")
class TodoListControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  TodoUserDetailsService userDetailsService;

  @MockBean
  TodoListService service;

  @Nested
  @DisplayName("todoList()のテスト")
  class TodoListTest {
    UserDetails user = new TodoUserDetails(new User("userId", "password", RoleName.TEST));

    @Test
    @DisplayName("パラメータに不正がある場合は例外をスローする")
    void illegalArgument() throws Exception {
      when(service.getDisplayList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
          Mockito.anyString())).thenThrow(new IllegalArgumentException("エラーメッセージ"));

      MvcResult result = mockMvc.perform(get("/todoList").with(user(user)).param("listType",
          "wrongStr").param("sort", "deadline").param("order", "ASC")).andReturn();

      assertThat(result.getResolvedException().getClass(), is(IllegalOperationException.class));
      verify(service, times(1)).getDisplayList(eq("userId"), eq("wrongStr"), eq("deadline"), eq(
          "ASC"));
    }

    @Test
    @DisplayName("パラメータが正常な場合、モデルに表示用データを格納してTodo一覧画面へ遷移する")
    void correctTransition() throws Exception {
      List<Todo> displayList = new ArrayList<>();

      when(service.getDisplayList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
          Mockito.anyString())).thenReturn(displayList);
      when(service.todayListSize(Mockito.anyString())).thenReturn(2);
      when(service.expiredListSize(Mockito.anyString())).thenReturn(3);

      MvcResult result = mockMvc.perform(get("/todoList").with(user(user)).param("listType",
          "normal").param("sort", "deadline").param("order", "ASC")).andExpect(view().name(
              "todo/todoList")).andReturn();
      Map<String, Object> models = result.getModelAndView().getModel();

      assertThat(models.get("displayList"), is(displayList));
      assertThat(models.get("todayListSize"), is(2));
      assertThat(models.get("expiredListSize"), is(3));
      assertThat(models.get("listType"), is("normal"));
      assertThat(models.get("sort"), is("deadline"));
      assertThat(models.get("order"), is("ASC"));

      verify(service, times(1)).getDisplayList(eq("userId"), eq("normal"), eq("deadline"), eq(
          "ASC"));
      verify(service, times(1)).getTodayList("userId");
      verify(service, times(1)).getExpiredList("userId");
    }
  }
}
