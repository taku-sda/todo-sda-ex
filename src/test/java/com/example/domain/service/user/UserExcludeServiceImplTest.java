package com.example.domain.service.user;

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
@DisplayName("UserExcludeServiceImplの単体テスト")
class UserExcludeServiceImplTest {

  @InjectMocks
  UserExcludeServiceImpl serviceImpl;

  @Mock
  UserRepository repository;

  @Mock
  HttpServletRequest httpServletRequest;

  @Nested
  @DisplayName("confirmPassword()のテスト")
  class TestconfirmPassword {
      //ハッシュ化パスワード
      String hashpass = new BCryptPasswordEncoder().encode("password");
      //登録済みと仮定するユーザー情報
      User existUser = new User("userId", hashpass, RoleName.USER);

    @Test
    @DisplayName("ユーザーIDが登録されていない場合、FailureConfirmExceptionが発生")
    void notExistUserId() {
      when(repository.findById("wrongUserId")).thenReturn(Optional.empty());

      assertThrows(FailureConfirmException.class,
          () -> serviceImpl.confirmPassword("wrongUserId", "password"));
      verify(repository, times(1)).findById("wrongUserId");
    }

    @Test
    @DisplayName("ユーザーIDに対して不正なパスワードの場合、FailureConfirmExceptionが発生")
    void unmatchPassword() {
      when(repository.findById("userId")).thenReturn(Optional.ofNullable(existUser));

      assertThrows(FailureConfirmException.class,
          () -> serviceImpl.confirmPassword("userId", "wrongPassword"));
      verify(repository, times(1)).findById("userId");
    }

    @Test
    @DisplayName("ユーザーIDに対して正しいパスワードの場合、例外が発生しない")
    void matchPassword() {
      when(repository.findById(existUser.getUserId())).thenReturn(Optional.ofNullable(existUser));

      assertDoesNotThrow(() ->
          serviceImpl.confirmPassword("userId", "password"));
      verify(repository, times(1)).findById("userId");
    }
  }

  @Nested
  @DisplayName("logoutWithHttpServletRequest()のテスト")
  class TestLogoutWithHttpServletRequest {
    @Test
    @DisplayName("ログアウトに失敗した場合はFailureAuthException例外が発生する")
    void failureLogout() throws ServletException {
      doThrow(ServletException.class).when(httpServletRequest).logout();

      assertThrows(FailureAuthException.class,
          () -> serviceImpl.logoutWithHttpServletRequest(httpServletRequest));
      verify(httpServletRequest, times(1)).logout();
    }

    @Test
    @DisplayName("ログアウトに成功した場合は例外が発生しない")
    void successLoout() throws ServletException {
      doNothing().when(httpServletRequest).logout();

      assertDoesNotThrow(() ->
          serviceImpl.logoutWithHttpServletRequest(httpServletRequest));
      verify(httpServletRequest, times(1)).logout();
    }
  }
}
