package com.example.app.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.model.RoleName;
import com.example.domain.model.User;
import com.example.domain.service.user.ExistUserException;
import com.example.domain.service.user.UserRegisterService;

/**
 * ユーザー登録操作のコントローラ.
 */
@Controller
@RequestMapping("/register")
public class UserRegisterController {
  @Autowired
  UserRegisterService userRegisterService;

  @ModelAttribute
  public UserForm setUpForm() {
    return new UserForm();
  }

  /**
   * ユーザー登録フォームへ遷移を行う.
   * @param model 連携するデータを格納
   * @return  登録フォーム画面
   */
  @GetMapping
  public String registerForm(Model model) {
    return "register/registerForm";
  }

  /**
   * フォームに入力されたユーザーが使用可能か確認する.
   * 使用可能なら登録確認,不可なら登録フォームへ遷移する
   * @param form  入力されたフォームクラス
   * @param result  バリデーションの結果
   * @param model 連携するデータを格納
   * @return  登録確認または登録フォーム画面
   */
  @PostMapping
  public String confirmAvailability(@Validated UserForm form, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return registerForm(model);
    }
    try {
      userRegisterService.confirmAvailability(form.getUserId());
    } catch (ExistUserException e) {
      model.addAttribute("error", e.getMessage());
      return registerForm(model);
    }

    model.addAttribute("title", "ToDo!! | ユーザー登録確認");
    return "register/confirmRegister";
  }

  /**
   * ユーザー登録処理を行う.
   * @param form  入力されたフォームクラス
   * @param model 連携するデータを格納
   * @param request 使用しているHttpServletRequestオブジェクト
   * @return ToDo一覧画面
   */
  @PostMapping("complete")
  public String completeRegister(UserForm form, Model model, HttpServletRequest request) {
    User registerUser = new User(form.getUserId(), form.getPassword(), RoleName.USER);
    userRegisterService.register(registerUser);
    userRegisterService.authWithHttpServletRequest(request, form.getUserId(), form.getPassword());

    return "todo/todoList";
  }
  
  /**
   * 入力内容を修正するためにユーザー登録画面に遷移する.
   * 入力されている内容をフォームクラスで受け渡す.
   * @param form  入力されたフォームクラス
   * @param model 連携するデータを格納
    * @return  登録フォーム画面
   */
  @PostMapping("fix")
  public String fixRegisterForm(UserForm form, Model model) {
    return "register/registerForm";
  }
}
