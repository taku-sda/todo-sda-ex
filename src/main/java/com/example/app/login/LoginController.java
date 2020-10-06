package com.example.app.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/loginForm")
public class LoginController {

  /**
   * ログインフォームへ遷移を行う.
   * @param model 連携するデータを格納
   * @return 遷移先のView名
   */
  @GetMapping
  public String loginForm(Model model) {
    model.addAttribute("title", "ToDo!! | ログインフォーム");
    return "login/loginForm";
  }
}
