package com.example.app.todo;

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
@DisplayName("ToDoFormの単体テスト")
class TodoFormTest {
  private TodoForm form = new TodoForm();
  BindingResult bindingResult = new BindException(form, "TodoForm");

  @Autowired
  Validator validator;

  @BeforeEach
  void setUp() {
    form.setTitle("タイトル");
    form.setDeadlineStr("2000-01-01T12:00");
    form.setPriority(3);
    form.setMemo("メモテキスト");
  }

  @Nested
  @DisplayName("入力内容が正しい場合")
  class CorrectInputs {
    @Test
    void correctInputs() {
      validator.validate(form, bindingResult);
      assertThat(bindingResult.hasErrors(), is(false));
    }
  }

  @Nested
  @DisplayName("入力内容に誤りがある場合")
  class wrongInputs {

    @ParameterizedTest
    @DisplayName("titleのバリデーション")
    @CsvSource({
        ", 'タイトルが未入力です'", //null
        "'aaaaaaaaaaaaaaaaaaaaa', '文字数オーバーです'" //20文字を超える
    })
    void titleValidation(String title, String message) {
      form.setTitle(title);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.getFieldErrors("title").stream()
              .anyMatch(it -> it.getDefaultMessage().equals(message)), is(true));
    }

    @ParameterizedTest
    @DisplayName("deadlineStrのバリデーション")
    @CsvSource({
        ", '期限が未入力です'", //null
        "'2000/01/01-12:00', '日時の入力内容が不正です'" //指定されたフォーマットではない
    })
    void deadlineStrValidation(String deadlineStr, String message) {
      form.setDeadlineStr(deadlineStr);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.getFieldErrors("deadlineStr").stream()
              .anyMatch(it -> it.getDefaultMessage().equals(message)), is(true));
    }

    @ParameterizedTest
    @DisplayName("priorityのバリデーション")
    @CsvSource({
        "0, '優先度の入力内容が不正です'", //最小値よりも小さい
        "6,  '優先度の入力内容が不正です'" //最大値よりも大きい
    })
    void priorityValidation(int priority, String message) {
      form.setPriority(priority);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.getFieldErrors("priority").stream()
              .anyMatch(it -> it.getDefaultMessage().equals(message)), is(true));
    }

    @Test
    @DisplayName("memoのバリデーション")
    void memoValidation() {
      //最大文字数を超えた401文字の文字列を用意
      StringBuilder sb = new StringBuilder();
      for(int i = 0; i < 401; i++) {
        sb.append("a");
      }
      String memo = sb.toString();

      form.setMemo(memo);
      validator.validate(form, bindingResult);

      assertThat(bindingResult.getFieldErrors("memo").stream()
              .anyMatch(it -> it.getDefaultMessage().equals("文字数オーバーです")), is(true));
    }
  }
}
