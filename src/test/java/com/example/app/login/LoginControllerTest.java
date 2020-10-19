package com.example.app.login;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.domain.service.user.TodoUserDetailsService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
@DisplayName("LoginControllerの単体テスト")
class LoginControllerTest {
  @Autowired
  MockMvc mockMvc;
  
  @MockBean
  TodoUserDetailsService  userDetailsService;

  @Test
  @DisplayName("ログインフォームに遷移する")
  void transitionLoginForm() throws Exception {
    mockMvc.perform(get("/loginForm"))
      .andExpect(status().isOk())
      .andExpect(view().name("login/loginForm"));
  }
}
