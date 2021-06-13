package com.example.domain.service.inquiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.example.app.inquiry.InquiryForm;

/**
 * お問い合わせ機能に関する処理を行うサービスクラス.
 */
@Service
public class InquiryServiceImpl implements InquiryService {

  @Autowired
  private MailSender sender;

  /** {@inheritDoc} */
  @Override
  public void sendMail(InquiryForm form) {
    SimpleMailMessage msg = new SimpleMailMessage();

    msg.setFrom(System.getenv("TODO_APP_MAIL_ADDRESS"));
    msg.setTo(System.getenv("TODO_APP_MAIL_ADDRESS"));
    msg.setSubject("フォームからのお問い合わせ : " + form.getType());
    msg.setText(form.getText());

    sender.send(msg);
  }

}
