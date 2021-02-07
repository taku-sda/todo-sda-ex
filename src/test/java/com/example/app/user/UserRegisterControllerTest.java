package com.example.app.user;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.domain.model.User;
import com.example.domain.service.user.ExistUserException;
import com.example.domain.service.user.FailureAuthException;
import com.example.domain.service.user.TodoUserDetailsService;
import com.example.domain.service.user.UserRegisterService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserRegisterController.class)
@DisplayName("UserRegisterControllerTestの単体テスト")
class UserRegisterControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockBean
  TodoUserDetailsService  userDetailsService;

  @MockBean
  UserRegisterService service;

  @Nested
  @DisplayName("registerForm()のテスト")
  class TestRegisterForm {
    @Test
    @DisplayName("UserFormがモデルに格納され、登録フォームに遷移する")
    void correctTransition() throws Exception {
      mockMvc.perform(get("/register"))
          .andExpect(status().isOk())
          .andExpect(model().attributeExists("userForm"))
          .andExpect(view().name("register/registerForm"));
    }
  }

  @Nested
  @DisplayName("confirmAvailability()のテスト")
  class TestConfirmAvailability {
    UserForm form = new UserForm();

    @BeforeEach
    void setUp() {
      form.setUserId("userId");
      form.setPassword("password");
      Mockito.reset(service);
    }

      @Nested
      @DisplayName("フォームの入力内容に誤りがある場合")
      class WrongFormInputs {
        @Test
        @DisplayName("バリデーションエラーが存在して、登録フォームに遷移する")
        void wrongFormInputsTransition() throws Exception {
          form.setUserId("aaaaaaaaaaa");
          
          mockMvc.perform((post("/register").with(csrf())).flashAttr("userForm", form))
              .andExpect(status().isOk())
              .andExpect(model().hasErrors())
              .andExpect(view().name("register/registerForm"));
        }
      }

      @Nested
      @DisplayName("フォームの入力内容が正しい場合")
      class RightFormInputs {
        @Test
        @DisplayName("ユーザーIDが重複している場合、エラーが存在して、登録フォームに遷移")
        void existUserId() throws Exception {
          doThrow(new ExistUserException("エラーメッセージ")).when(service)
            .confirmAvailability("userId");

          mockMvc.perform(post("/register").with(csrf()).flashAttr("userForm", form))
              .andExpect(status().isOk())
              .andExpect(model().attributeExists("error"))
              .andExpect(view().name("register/registerForm"));

          verify(service, times(1)).confirmAvailability(Mockito.anyString());
        }

        @Test
        @DisplayName("ユーザーIDが利用可能な場合、登録確認ページへ遷移")
        void correctTransition() throws Exception {
          doNothing().when(service).confirmAvailability("userId");

          mockMvc.perform(post("/register").with(csrf()).flashAttr("userForm", form))
              .andExpect(status().isOk())
              .andExpect(view().name("register/confirmRegister"));

          verify(service, times(1)).confirmAvailability(Mockito.anyString());
        }
      }
  }

  @Nested
  @DisplayName("completeRegister()のテスト")
  class TestCompleteRegister {
    UserForm form = new UserForm();

    @BeforeEach
    void setUp() {
      form.setUserId("userId");
      form.setPassword("password");
      Mockito.reset(service);
    }

    @Test
    @DisplayName("登録失敗時にエラーページに遷移")
    void failureRegister() throws Exception {
      //DBサーバーが利用できず、DataAccessResourceFailureExceptionが発生した場合を想定
      when(service.register((User) Mockito.any()))
          .thenThrow(DataAccessResourceFailureException.class);

      mockMvc.perform(post("/register/complete").with(csrf()).flashAttr("userForm", form))
          .andExpect(status().isOk())
          .andExpect(model().attributeExists("errorMessage"))
          .andExpect(view().name("error/error"));

      verify(service, times(1)).register((User) Mockito.any());
      verify(service, times(0)).authWithHttpServletRequest(Mockito.any(), eq("userId"), eq("password"));
    }

    @Test
    @DisplayName("登録後の自動ログインに失敗した場合、エラーページに遷移")
    void failureLogin() throws Exception {
      doThrow(new FailureAuthException("エラーメッセージ"))
          .when(service).authWithHttpServletRequest(Mockito.any(), eq("userId"), eq("password"));

      mockMvc.perform(post("/register/complete").with(csrf()).flashAttr("userForm", form))
          .andExpect(status().isOk())
          .andExpect(model().attributeExists("errorMessage"))
          .andExpect(view().name("error/error"));

      verify(service, times(1)).register((User) Mockito.any());
      verify(service, times(1)).authWithHttpServletRequest(Mockito.any(), eq("userId"), eq("password"));
    }

    @Test
    @DisplayName("正常に処理が完了した場合、ToDo一覧ページに遷移")
    void successRegister() throws Exception {
      mockMvc.perform(post("/register/complete").with(csrf()).flashAttr("userForm", form))
      .andExpect(status().isFound())
      .andExpect(view().name("redirect:/todoList"));

      verify(service, times(1)).register((User) Mockito.any());
      verify(service, times(1)).authWithHttpServletRequest(Mockito.any(), eq("userId"), eq("password"));
    }
  }
}
