package com.example.app.user;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.config.WebMvcControllerAdvice;
import com.example.domain.model.User;
import com.example.domain.service.user.ExistUserException;
import com.example.domain.service.user.FailureAuthException;
import com.example.domain.service.user.UserRegisterService;

@SpringBootTest
@DisplayName("UserRegisterControllerTestの結合テスト")
class UserRegisterControllerTest {

  private MockMvc mockMvc;

  @InjectMocks
  UserRegisterController target;

  @Mock
  UserRegisterService service;

  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(target)
        .setControllerAdvice(new WebMvcControllerAdvice())
        .build();
  }

  @Nested
  @DisplayName("registerForm()のテスト")
  class TestRegisterForm {
    @Test
    @DisplayName("UserFormがモデルに格納され、登録フォームに遷移する")
    void correctTransition() throws Exception {
      mockMvc.perform(get("/register")).andExpect(status().isOk())
          .andExpect(model().attributeExists("userForm"))
          .andExpect(forwardedUrl("register/registerForm"));
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
    }

      @Nested
      @DisplayName("フォームの入力内容に誤りがある場合")
      class WrongFormInputs {
        @Test
        @DisplayName("バリデーションエラーが存在して、登録フォームに遷移する")
        void wrongFormInputsTransition() throws Exception {
          form.setUserId("aaaaaaaaaaa");
          
          mockMvc.perform((post("/register")).flashAttr("userForm", form))
          .andExpect(model().hasErrors())
          .andExpect(status().isOk())
          .andExpect(forwardedUrl("register/registerForm"));
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

          mockMvc.perform(post("/register").flashAttr("userForm", form))
              .andExpect(model().attributeExists("error"))
              .andExpect(status().isOk())
              .andExpect(forwardedUrl("register/registerForm"));

          verify(service, times(1)).confirmAvailability(Mockito.anyString());
        }

        @Test
        @DisplayName("ユーザーIDが利用可能な場合、登録確認ページへ遷移")
        void correctTransition() throws Exception {
          doNothing().when(service).confirmAvailability("userId");

          mockMvc.perform(post("/register").flashAttr("userForm", form))
              .andExpect(status().isOk())
              .andExpect(forwardedUrl("register/confirmRegister"));

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
    }

    @Test
    @DisplayName("登録失敗時にエラーページに遷移")
    void failureRegister() throws Exception {
      //DBサーバーが利用できず、DataAccessResourceFailureExceptionが発生した場合を想定
      when(service.register((User) Mockito.any()))
          .thenThrow(DataAccessResourceFailureException.class);

      mockMvc.perform(post("/register/complete").flashAttr("userForm", form))
          .andExpect(model().attributeExists("errorMessage"))
          .andExpect(status().isOk())
          .andExpect(forwardedUrl("error/error"));

      verify(service, times(1)).register((User) Mockito.any());
      verify(service, times(0)).authWithHttpServletRequest(Mockito.any(), eq("userId"), eq("password"));
    }

    @Test
    @DisplayName("登録後の自動ログインに失敗した場合、エラーページに遷移")
    void failureLogin() throws Exception {
      doThrow(new FailureAuthException("エラーメッセージ"))
          .when(service).authWithHttpServletRequest(Mockito.any(), eq("userId"), eq("password"));

      mockMvc.perform(post("/register/complete").flashAttr("userForm", form))
          .andExpect(model().attributeExists("errorMessage"))
          .andExpect(status().isOk())
          .andExpect(forwardedUrl("error/error"));

      verify(service, times(1)).register((User) Mockito.any());
      verify(service, times(1)).authWithHttpServletRequest(Mockito.any(), eq("userId"), eq("password"));
    }

    @Test
    @DisplayName("正常に処理が完了した場合、ToDo一覧ページに遷移")
    void successRegister() throws Exception {
      mockMvc.perform(post("/register/complete").flashAttr("userForm", form))
      .andExpect(status().isOk())
      .andExpect(forwardedUrl("todo/todoList"));

      verify(service, times(1)).register((User) Mockito.any());
      verify(service, times(1)).authWithHttpServletRequest(Mockito.any(), eq("userId"), eq("password"));
    }
  }
}
