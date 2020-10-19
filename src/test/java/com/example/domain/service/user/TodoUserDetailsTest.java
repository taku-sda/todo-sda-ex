package com.example.domain.service.user;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.domain.model.RoleName;
import com.example.domain.model.User;

@DisplayName("TodoUserDetailsのテスト")
class TodoUserDetailsTest {

  @Test
  @DisplayName("値の設定、取得が正しく行える")
  void getCorrectData() {
    User user = new User("userId", "password", RoleName.USER);

    TodoUserDetails userDetails = new TodoUserDetails(user);
    assertThat(userDetails.getAuthorities().stream()
        .anyMatch(it -> it.getAuthority().equals("ROLE_USER")), is(true));
    assertThat(userDetails.getUsername(), is("userId"));
    assertThat(userDetails.getPassword(), is("password"));
    assertThat(userDetails.isAccountNonExpired(), is(true));
    assertThat(userDetails.isAccountNonLocked(), is(true));
    assertThat(userDetails.isCredentialsNonExpired(), is(true));
    assertThat(userDetails.isEnabled(), is(true));
  }
}
