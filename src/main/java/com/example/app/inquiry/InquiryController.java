package com.example.app.inquiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.service.inquiry.InquiryService;

/**
 * お問い合わせ機能に関する処理を行うコントローラ.
 */
@Controller
@RequestMapping("/inquiry")
public class InquiryController {

  @Autowired
  InquiryService inquiryService;

  @ModelAttribute
  public InquiryForm setUpForm() {
    return new InquiryForm();
  }

  /**
   * お問い合わせ画面に遷移する.
   * @param model モデル
   * @return お問い合わせ画面
   */
  @GetMapping
  String inquiryForm(Model model) {
    return "inquiry/inquiry";
  }

  /**
   * フォームの入力内容を基にお問い合わせを送信する. 
   * 入力内容に不備がある場合はフォワードでお問い合わせ画面に遷移.
   * 送信が完了した場合はリダイレクトでお問い合わせ画面に遷移.
   *  メール送信処理でエラーが発生した場合はエラー画面に遷移.
   * @param form お問い合わせフォーム
   * @param result フォームのバリデーション結果
   * @param model モデル
   * @param redirectAttributes リダイレクト先との連携データを格納
   * @return お問い合わせ画面(メール処理エラー時はエラー画面)
   */
  @PostMapping
  String sendInquiry(@Validated InquiryForm form, BindingResult result, Model model,
      RedirectAttributes redirectAttributes) {

    if (result.hasErrors()) {
      model.addAttribute("inquiryMsg", "お問い合わせが送信できませんでした");
      return inquiryForm(model);
    }

    try {
      inquiryService.sendMail(form);
    } catch (MailException e) {
      model.addAttribute("errorMessage", "メールの送信処理でエラーが発生しました");
      return "error/error";
    }

    redirectAttributes.addFlashAttribute("inquiryMsg", "お問い合わせを送信しました");
    return "redirect:/inquiry";
  }
}
