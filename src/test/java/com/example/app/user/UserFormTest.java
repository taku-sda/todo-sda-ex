package com.example.app.user;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@SpringBootTest
@DisplayName("UserFormの単体テスト")
class UserFormTest {
  private UserForm form = new UserForm();
  BindingResult bindingResult = new BindException(form, "UserForm");
  
  @Autowired
  Validator validator;

  @BeforeEach
  void setUp() {
    form.setUserId("userId");
    form.setPassword("password");
  }

  @Nested
  @DisplayName("userIdのバリデーションのテスト")
  class TestUserId {

    @Test
    @DisplayName("入力値が正しい場合")
    void deckValidation() {
      validator.validate(form, bindingResult);
      assertThat(bindingResult.hasFieldErrors("userId"), is(false));
    }

    @ParameterizedTest
    @DisplayName("入力値が不正の場合")
    @CsvSource({
        ", 'ユーザーIDが未入力です'", //null
        "''ユーザーID, 'ユーザーIDは英数字10文字以内です'", //英数字でない
        "'aaaaaaaaaaa', 'ユーザーIDは英数字10文字以内です'" //10文字を超える
    })
    void deckValidation(String userId, String message) {
      form.setUserId(userId);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("userId"), is(true));
      assertThat(bindingResult.getFieldErrors("userId").stream()
              .anyMatch(it -> it.getDefaultMessage().equals(message)), is(true));
    }
  }

  @Nested
  @DisplayName("passwordのバリデーションのテスト")
  class TestPassword {
    @Test
    @DisplayName("入力値が正しい場合")
    void deckValidation() {
      validator.validate(form, bindingResult);
      assertThat(bindingResult.hasFieldErrors("password"), is(false));
    }

    @ParameterizedTest
    @DisplayName("入力値が不正の場合")
    @CsvSource({
        ", 'パスワードが未入力です'",  //null
        "'パスワード', 'パスワードは英数字4～8文字です'",  //英数字でない
        "'aaa', 'パスワードは英数字4～8文字です'",  //4文字に満たない
        "'aaaaaaaaa', 'パスワードは英数字4～8文字です'" //8文字を超える
    })
    void deckValidation(String password, String message) {
      form.setPassword(password);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("password"), is(true));
      assertThat(bindingResult.getFieldErrors("password").stream()
              .anyMatch(it -> it.getDefaultMessage().equals(message)), is(true));
    }
  }
}
