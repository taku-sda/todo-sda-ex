package com.example.app.inquiry;

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
@DisplayName("InquiryFormの単体テスト")
class InquiryFormTest {

  private InquiryForm form = new InquiryForm();
  BindingResult bindingResult = new BindException(form, "InquiryForm");

  @Autowired
  Validator validator;

  @BeforeEach
  void setUp() {
    form.setName("お問い合わせユーザー名");
    form.setEmail("InquiryTest@mail.com");
    form.setType("ご意見・ご要望");
    form.setText("お問い合わせの本文");
  }

  @DisplayName("全ての入力項目が適切である場合、エラーが存在しない")
  void allItemValidationTrue() {
    validator.validate(form, bindingResult);

    assertThat(bindingResult.hasErrors(), is(false));
  }

  @Nested
  @DisplayName("nameのバリデーションテスト")
  class TestName {

    @Test
    @DisplayName("入力内容が空(null)の場合、エラーが存在する")
    void nameValidationFalseForBlank() {
      form.setName(null);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("name"), is(true));
      assertThat(bindingResult.getFieldErrors("name").stream().anyMatch(it -> it.getDefaultMessage()
          .equals("お名前が入力されていません")), is(true));
    }

    @Test
    @DisplayName("入力内容が文字数オーバーの場合、エラーが存在する")
    void nameValidationFalseForOver() {
      // 最大文字数を超えた51文字の文字列を用意
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < 51; i++) {
        sb.append("a");
      }
      String longStr = sb.toString();

      form.setName(longStr);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("name"), is(true));
      assertThat(bindingResult.getFieldErrors("name").stream().anyMatch(it -> it.getDefaultMessage()
          .equals("文字数オーバーです")), is(true));
    }
  }

  @Nested
  @DisplayName("emailのバリデーションテスト")
  class TestEmail {

    @Test
    @DisplayName("入力内容が空(null)の場合、エラーが存在する")
    void emailValidationFalseForBlank() {
      form.setEmail(null);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("email"), is(true));
      assertThat(bindingResult.getFieldErrors("email").stream().anyMatch(it -> it
          .getDefaultMessage().equals("メールアドレスが入力されていません")), is(true));
    }

    @Test
    @DisplayName("入力内容が文字数オーバーの場合、エラーが存在する")
    void emailValidationFalseForOver() {
      // 最大文字数を超えた101文字の文字列を用意
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < 101; i++) {
        sb.append("a");
      }
      String longStr = sb.toString();

      form.setEmail(longStr);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("email"), is(true));
      assertThat(bindingResult.getFieldErrors("email").stream().anyMatch(it -> it
          .getDefaultMessage().equals("文字数オーバーです")), is(true));
    }

    @Test
    @DisplayName("入力内容がメールアドレスとして適切でない場合、エラーが存在する")
    void emailValidationFalseForFormat() {
      form.setEmail("WrongMail$mail.com");
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("email"), is(true));
      assertThat(bindingResult.getFieldErrors("email").stream().anyMatch(it -> it
          .getDefaultMessage().equals("メールアドレスが適切ではありません")), is(true));
    }
  }

  @Nested
  @DisplayName("typeのバリデーションテスト")
  class TestType {

    @ParameterizedTest
    @DisplayName("分類の選択肢に存在する入力内容の場合、エラーが存在しない")
    @CsvSource({ "ご意見・ご要望", "不具合について", "その他" })
    void typeValidationTrue(String type) {
      form.setType(type);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("type"), is(false));
    }

    @Test
    @DisplayName("分類の選択肢に存在しない入力内容の場合、エラーが存在する")
    void typeValidationFalse() {
      form.setType("不正な分類");
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("type"), is(true));
      assertThat(bindingResult.getFieldErrors("type").stream().anyMatch(it -> it.getDefaultMessage()
          .equals("お問い合わせの分類が不正です")), is(true));
    }
  }

  @Nested
  @DisplayName("textのバリデーションテスト")
  class TestText {

    @Test
    @DisplayName("入力内容が空(null)の場合、エラーが存在する")
    void textValidationFalseForBlank() {
      form.setText(null);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("text"), is(true));
      assertThat(bindingResult.getFieldErrors("text").stream().anyMatch(it -> it.getDefaultMessage()
          .equals("お問い合わせの内容が入力されていません")), is(true));
    }

    @Test
    @DisplayName("入力内容が文字数オーバーの場合、エラーが存在する")
    void textValidationFalseForOver() {
      // 最大文字数を超えた401文字の文字列を用意
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < 401; i++) {
        sb.append("a");
      }
      String longStr = sb.toString();

      form.setText(longStr);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("text"), is(true));
      assertThat(bindingResult.getFieldErrors("text").stream().anyMatch(it -> it.getDefaultMessage()
          .equals("文字数オーバーです")), is(true));
    }
  }
}
