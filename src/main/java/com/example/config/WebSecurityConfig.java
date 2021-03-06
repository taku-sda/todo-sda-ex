package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.domain.service.user.TodoUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
      //ユーザー登録画面は全て許可
      .antMatchers("/register", "/register/**").permitAll()
      //お問い合わせ送信は全て許可
      .antMatchers("/inquiry", "/inquiry/**").permitAll()
      .antMatchers("/js/**", "/css/**", "/img/**").permitAll()
      .antMatchers("/**").authenticated()
      .and()
      .formLogin()
      .loginPage("/loginForm")
      .loginProcessingUrl("/login")
      .usernameParameter("userId")
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
