package com.example.app.login;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@Transactional
@DisplayName("ユーザー認証に関する結合テスト")
class AuthTest {
  private MockMvc mockMvc;

  @Autowired
  WebApplicationContext context;

  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @Nested
  @DisplayName("ログイン機能のテスト")
  class LoginTest {
    @Test
    @DisplayName("登録済みのテストユーザーで正しく認証処理が行われる")
    void registeredUserLogin() throws Exception {
      mockMvc.perform(formLogin().user("userid", "testuser").password("todotest"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/todoList"))
        .andExpect(authenticated().withRoles("TEST"));
    }

    @Test
    @DisplayName("未登録のユーザーで認証に失敗する")
    void notRegisteredUserLogin() throws Exception {
      mockMvc.perform(formLogin().user("userid", "hoge").password("hoge"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/loginForm?error=ture"))
        .andExpect(unauthenticated());
    }
  }

  @Nested
  @DisplayName("ログアウト機能のテスト")
  class LogoutTest {
    @Test
    @WithMockUser
    @DisplayName("ログアウト処理が正しく行われる")
    void  testLogout() throws Exception {
      mockMvc.perform(logout())
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"))
        .andExpect(unauthenticated());
    }
  }
}
