package com.example.domain.service.inquiry;

import com.example.app.inquiry.InquiryForm;

public interface InquiryService {

  /**
   * お問い合わせフォームの内容をメールで送信する.
   * @param form 入力されたお問い合わせフォーム
   */
  void sendMail(InquiryForm form);
}
