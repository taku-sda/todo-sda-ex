package com.example.app.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.service.user.FailureConfirmException;
import com.example.domain.service.user.UserExcludeService;

/**
 * ユーザー登録解除操作のコントローラ.
 */
@Controller
@RequestMapping("/exclude")
public class UserExcludeController {
  @Autowired
  UserExcludeService service;

  @ModelAttribute
  public UserForm setUpForm() {
    return new UserForm();
  }

  /**
   * ユーザー登録解除フォームへ遷移を行う.
   * 一般ユーザーのロールのみアクセス可能.
   * @param model 連携するデータを格納
   * @return  登録解除フォーム画面
   */
  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public String excludeForm(Model model) {
    return "exclude/excludeForm";
  }

  /**
   * ユーザー登録解除を行う.
   * 一般ユーザーのロールのみアクセス可能.<br>
   * ユーザー情報に誤りがある場合はユーザー登録解除フォームへ遷移する.<br>
   * 登録解除に成功した場合は登録解除完了画面に遷移する.<br>
   * @param form  入力されたフォームクラス
   * @param result  フォームクラスのバリデーションの結果
   * @param model 連携するデータを格納
   * @param request 使用しているHttpServletRequestオブジェクト
   * @return  遷移先のView名
   */
  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public String completeExclude(@Validated UserForm form, BindingResult result,
      Model model, HttpServletRequest request) {

    if (result.hasErrors()) {
      return excludeForm(model);
    }

    try {
      service.confirmPassword(form.getUserId(), form.getPassword());
    } catch (FailureConfirmException e) {
      model.addAttribute("error", e.getMessage());
      return excludeForm(model);
    }

    service.exclude(form.getUserId());
    service.logoutWithHttpServletRequest(request);

    return "exclude/completeExclude";
  }
}
