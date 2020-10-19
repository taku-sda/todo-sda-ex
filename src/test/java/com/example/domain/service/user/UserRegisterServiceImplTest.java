package com.example.domain.service.user;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.domain.model.RoleName;
import com.example.domain.model.User;
import com.example.domain.repository.user.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserRegisterServiceImplの単体テスト")
class UserRegisterServiceImplTest {
  @InjectMocks
  UserRegisterServiceImpl serviceImpl;

  @Mock
  UserRepository repository;

  @Mock
  HttpServletRequest httpServletRequest;

  @Nested
  @DisplayName("confirmAvailability()のテスト")
  class TestConfirmAvailability {
    @Test
    @DisplayName("ユーザーが重複している場合はExistUserException例外を出力する")
    void existUser() {
      User existUser = new User("userId", "password", RoleName.USER);
      when(repository.findById("userId")).thenReturn(Optional.ofNullable(existUser));

      ExistUserException ex = assertThrows(ExistUserException.class,
          () -> serviceImpl.confirmAvailability("userId"));
      assertThat(ex.getMessage(), is("そのユーザーIDは既に使用されています。"));
    }

    @Test
    @DisplayName("ユーザーが重複していない場合は例外が発生しない")
    void notExistUser() {
      when(repository.findById("userId")).thenReturn(Optional.empty());

      assertDoesNotThrow(() -> serviceImpl.confirmAvailability("userId"));
    }
  }

  @Nested
  @DisplayName("register()のテスト")
  class TestRegister {
    @Test
    @DisplayName("正しいユーザー情報が登録される")
    void correctUserReistered() {
      User registerUser = new User("userId", "password", RoleName.USER);

      User actualUser = serviceImpl.register(registerUser);

      assertThat(actualUser.getUserId(), is("userId"));
      assertThat(new BCryptPasswordEncoder()
          .matches("password", actualUser.getPassword()), is(true));
      assertThat(actualUser.getRoleName(), is(RoleName.USER));

      verify(repository, times(1)).save(actualUser);
    }
  }

  @Nested
  @DisplayName("authWithHttpServletRequest()のテスト")
  class TestAuthWithHttpServletRequest {
    @Test
    @DisplayName("認証に失敗した場合はFailureLoginException例外を出力する")
    void failureLogin() throws ServletException {
      doThrow(ServletException.class).when(httpServletRequest).login("userId", "password");

      assertThrows(FailureAuthException.class,
          () -> serviceImpl.authWithHttpServletRequest(httpServletRequest, "userId", "password"));

      verify(httpServletRequest, times(1)).login("userId", "password");
    }

    @Test
    @DisplayName("認証に成功した場合は例外が発生しない")
    void successLogin() throws ServletException {
      doNothing().when(httpServletRequest).login("userId", "password");

      assertDoesNotThrow(() ->
          serviceImpl.authWithHttpServletRequest(httpServletRequest, "userId", "password"));

      verify(httpServletRequest, times(1)).login("userId", "password");
    }
  }
}
