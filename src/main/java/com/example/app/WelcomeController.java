package com.example.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * トップページのコントローラ.
 */
@Controller
@RequestMapping("/")
public class WelcomeController {

  /**
   * トップページへ遷移を行う.
   * @param model 連携するデータを格納
   * @return 遷移先のView名
   */
  @GetMapping
  public String index(Model model) {
    return "index";
  }
}
