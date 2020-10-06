package com.example.domain.service.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.domain.model.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
public class TodoUserDetails implements UserDetails {
  /** 認証ユーザー.*/
  @Getter
  private final User user;

  /**
   * ${@inheritDoc}.
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList("ROLE_"
        + this.user.getRoleName().name());
  }

  /**
   * ${@inheritDoc}.
   */
  @Override
  public String getPassword() {
    return this.user.getPassword();
  }

  /**
   * ${@inheritDoc}.
   */
  @Override
  public String getUsername() {
    return this.user.getUserId();
  }

  /**
   * ${@inheritDoc}.
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * ${@inheritDoc}.
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * ${@inheritDoc}.
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * ${@inheritDoc}.
   */
  @Override
  public boolean isEnabled() {
    return true;
  }
}
