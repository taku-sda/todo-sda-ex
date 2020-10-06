package com.example.app.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("loginForm")
public class LoginController {
  public String loginForm(Model model) {
    model.addAttribute("title", "ToDo!! | ログインフォーム");
    return "login/loginForm";
  }
}
