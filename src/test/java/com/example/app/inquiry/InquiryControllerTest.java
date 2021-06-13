package com.example.app.inquiry;

import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSendException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.domain.service.inquiry.InquiryService;
import com.example.domain.service.user.TodoUserDetailsService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InquiryController.class)
@DisplayName("InquiryControllerTestの単体テスト")
class InquiryControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  TodoUserDetailsService userDetailsService;

  @MockBean
  InquiryService service;

  @Nested
  @DisplayName("inquiryForm()のテスト")
  class TestInquiryForm {

    @Test
    @DisplayName("お問い合わせフォームがモデルに格納され、お問い合わせ画面に遷移する")
    void correctTransition() throws Exception {
      mockMvc.perform(get("/inquiry")).andExpect(status().isOk()).andExpect(model().attributeExists(
          "inquiryForm")).andExpect(view().name("inquiry/inquiry"));
    }
  }

  @Nested
  @DisplayName("sendInquiry()のテスト")
  class TestSendInquiry {
    InquiryForm form = new InquiryForm();

    @BeforeEach
    void setUp() {
      form.setType("ご意見・ご要望");
      form.setText("お問い合わせ本文");
    }

    @Nested
    @DisplayName("フォームの入力内容にエラーがある場合")
    class WrongFormInputs {
      @Test
      @DisplayName("バリデーションエラーが存在して、お問い合わせ画面にフォワードする")
      void wrongFormInputsTransition() throws Exception {
        form.setType("不正な分類");

        mockMvc.perform(post("/inquiry").with(csrf()).flashAttr("inquiryForm", form)).andExpect(
            status().isOk()).andExpect(model().hasErrors()).andExpect(view().name(
                "inquiry/inquiry"));
      }
    }

    @Nested
    @DisplayName("フォームの入力内容が正しい場合")
    class RightFormInputs {

      @Test
      @DisplayName("メールの送信処理でエラーが発生した場合、エラーページへ遷移する")
      void MailSendingError() throws Exception {
        doThrow(new MailSendException("エラーメッセージ")).when(service).sendMail(form);

        mockMvc.perform(post("/inquiry").with(csrf()).flashAttr("inquiryForm", form)).andExpect(
            status().isOk()).andExpect(model().attributeExists("errorMessage")).andExpect(view().name(
                "error/error"));
      }

      @Test
      @DisplayName("正常に処理が完了した場合、お問い合わせ画面にリダイレクト")
      void successSendInquiry() throws Exception {
        mockMvc.perform(post("/inquiry").with(csrf()).flashAttr("inquiryForm", form)).andExpect(
            status().isFound()).andExpect(view().name("redirect:/inquiry"));
      }
    }
  }
}
