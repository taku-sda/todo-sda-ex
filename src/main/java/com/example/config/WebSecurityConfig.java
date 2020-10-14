package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.domain.service.user.TodoUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
   /** UserDetailsServiceの実装クラス.*/
  @Autowired
  TodoUserDetailsService  userDetailsService;

  /**
   * パスワードのハッシュ化を行うエンコーダーを指定.
   * @return 使用するエンコーダー
   */
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * ${@inheritDoc}.
   */
  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .antMatchers("/").permitAll()
      .antMatchers("/register", "/register/complete").permitAll()
      .antMatchers("js/**", "css/**").permitAll()
      .antMatchers("/**").authenticated()
      .and()
      .formLogin()
      .loginPage("/loginForm")
      .loginProcessingUrl("/login")
      .usernameParameter("userid")
      .passwordParameter("password")
      .defaultSuccessUrl("/todoList", true)
      .failureUrl("/loginForm?error=ture").permitAll()
      .and()
      .logout()
      .logoutSuccessUrl("/").permitAll();
  }

  /**
   * ${@inheritDoc}.
   */
  @Override
  protected void configure(final AuthenticationManagerBuilder auth)
      throws Exception {
    auth.userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder());
  }
}
