package com.example.app.user;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.domain.service.user.FailureAuthException;
import com.example.domain.service.user.FailureConfirmException;
import com.example.domain.service.user.TodoUserDetailsService;
import com.example.domain.service.user.UserExcludeService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserExcludeController.class)
@DisplayName("UserExcludeControllerの単体テスト")
class UserExcludeControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockBean
  TodoUserDetailsService  userDetailsService;

  @MockBean
  UserExcludeService service;

  @Nested
  @WithMockUser(roles = "TEST")
  @DisplayName("テストユーザーでアクセスした場合")
  class TestUserAccess {
    @Test
    @DisplayName("excludeForm()にアクセスを拒否される")
    void accessDeniedByExcludeForm() throws Exception {
      mockMvc.perform(get("/exclude").with(csrf()))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("completeExclude()にアクセスを拒否される")
    void accessDeniedByCompleteExclude() throws Exception {
      mockMvc.perform(post("/exclude").with(csrf()))
          .andExpect(status().isForbidden());
    }
  }

  @Nested
  @DisplayName("一般ユーザーでアクセスした場合")
  class GeneralUserAccess {
    @Nested
    @WithMockUser(roles = "USER")
    @DisplayName("excludeForm()のテスト")
    class TestExcludeForm {
      @Test
      @DisplayName("UserFormがモデルに格納され、登録解除フォームに遷移する")
      void correctTransition() throws Exception {
        mockMvc.perform(get("/exclude").with(csrf())).andExpect(status().isOk())
            .andExpect(model().attributeExists("userForm"))
            .andExpect(view().name("exclude/excludeForm"));
      }
    }

    @Nested
    @WithMockUser(roles = "USER")
    @DisplayName("completeExclude()のテスト")
    class TestCompleteExclude {
      UserForm form = new UserForm();

      @BeforeEach
      void setUp() {
        form.setUserId("user");
        form.setPassword("password");
        Mockito.reset(service);
      }

      @Test
      @DisplayName("フォームの入力内容に誤りがある場合、エラーが存在して、解除フォームに遷移")
      void wrongFormInputs() throws Exception {
        form.setPassword("英数字ではないパスワード");

        mockMvc.perform((post("/exclude").with(csrf())).flashAttr("userForm", form))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("userForm"))
            .andExpect(model().hasErrors())
            .andExpect(view().name("exclude/excludeForm"));
      }

      @Test
      @DisplayName("ユーザー情報が正しくない場合、エラーが存在して、解除フォームに遷移")
      void wrongUserInfo() throws Exception {
        doThrow(new FailureConfirmException("エラーメッセージ"))
            .when(service).confirmPassword("user", "password");

        mockMvc.perform((post("/exclude").with(csrf())).flashAttr("userForm", form))
        .andExpect(status().isOk())
            .andExpect(model().attributeExists("error"))
            .andExpect(view().name("exclude/excludeForm"));

        verify(service, times(1)).confirmPassword("user", "password");
      }

      @Test
      @DisplayName("登録後の自動ログアウトに失敗した場合、エラーページに遷移")
      void failureLogout() throws Exception {
        doNothing().when(service).confirmPassword("user", "password");
        doThrow(new FailureAuthException("エラーメッセージ"))
            .when(service).logoutWithHttpServletRequest(Mockito.any());

        mockMvc.perform((post("/exclude").with(csrf())).flashAttr("userForm", form))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("errorMessage"))
            .andExpect(view().name("error/error"));

        verify(service, times(1)).confirmPassword("user", "password");
        verify(service, times(1)).exclude("user");
        verify(service, times(1)).logoutWithHttpServletRequest(Mockito.any());
      }

      @Test
      @DisplayName("正常に処理が完了した場合、ToDo一覧ページに遷移")
      void successExclude() throws Exception {
        doNothing().when(service).confirmPassword("user", "password");
        doNothing().when(service).logoutWithHttpServletRequest(Mockito.any());

        mockMvc.perform((post("/exclude").with(csrf())).flashAttr("userForm", form))
        .andExpect(status().isOk())
        .andExpect(view().name("exclude/completeExclude"));

        verify(service, times(1)).confirmPassword("user", "password");
        verify(service, times(1)).exclude("user");
        verify(service, times(1)).logoutWithHttpServletRequest(Mockito.any());
      }
    }
  }
}
