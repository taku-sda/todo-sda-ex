package com.example.domain.service.user;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.domain.model.RoleName;
import com.example.domain.model.User;
import com.example.domain.repository.user.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoUserDetailsServiceの単体テスト")
class TodoUserDetailsServiceTest {
  @InjectMocks
  TodoUserDetailsService service;

  @Mock
  UserRepository repository;

  @Test
  @DisplayName("ユーザーIDが存在する場合に正しいUser情報を取得")
  void userFound() {
    User user = new User("userId", "password", RoleName.USER);
    when(repository.findById("userId")).thenReturn(Optional.ofNullable(user));

    UserDetails actual = service.loadUserByUsername("userId");

    assertThat(actual.getUsername(), is("userId"));
    assertThat(actual.getPassword(), is("password"));
    assertThat(actual.getAuthorities().stream()
        .anyMatch(it -> it.getAuthority().equals("ROLE_USER")), is(true));
  }

  @Test
  @DisplayName("ユーザーIDが存在しない場合に例外が発生")
  void userNotFound() {
    when(repository.findById("userId")).thenReturn(Optional.empty());

    UsernameNotFoundException exception =
        assertThrows(UsernameNotFoundException.class,
            () -> service.loadUserByUsername("userId"));
    assertThat(exception.getMessage(), is("userId is not found."));
  }
}
