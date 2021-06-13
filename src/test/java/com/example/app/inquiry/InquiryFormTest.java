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
    form.setType("ご意見・ご要望");
    form.setText("お問い合わせの本文");
  }

  @Nested
  @DisplayName("typeのバリデーションテスト")
  class TestType {

    @ParameterizedTest
    @DisplayName("正しい分類が指定されている場合")
    @CsvSource({ "ご意見・ご要望", "不具合について", "その他" })
    void typeValidationTrue(String type) {
      form.setType(type);
      validator.validate(form, bindingResult);
      assertThat(bindingResult.hasFieldErrors("type"), is(false));
    }

    @Test
    @DisplayName("入力内容が不正の場合")
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
    @DisplayName("入力内容が正しい場合")
    void textValidationTrue() {
      validator.validate(form, bindingResult);
      assertThat(bindingResult.hasFieldErrors("text"), is(false));
    }

    @Test
    @DisplayName("入力内容が空(null)の場合")
    void textValidationFalseForBlank() {
      form.setText(null);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("text"), is(true));
      assertThat(bindingResult.getFieldErrors("text").stream().anyMatch(it -> it.getDefaultMessage()
          .equals("お問い合わせの内容が入力されていません")), is(true));
    }

    @Test
    @DisplayName("入力内容が文字数オーバーの場合")
    void textValidationFalseForOver() {
      // 最大文字数を超えた401文字の文字列を用意
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < 401; i++) {
        sb.append("a");
      }
      String memo = sb.toString();

      form.setText(memo);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.hasFieldErrors("text"), is(true));
      assertThat(bindingResult.getFieldErrors("text").stream().anyMatch(it -> it.getDefaultMessage()
          .equals("文字数オーバーです")), is(true));
    }
  }
}
