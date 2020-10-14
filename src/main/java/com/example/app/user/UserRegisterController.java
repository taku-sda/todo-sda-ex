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

  @GetMapping
  public String registerForm(Model model) {
    model.addAttribute("title", "ToDo!! | ユーザー登録フォーム");
    return "register/registerForm";
  }

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

  @PostMapping("complete")
  public String completeRegister(UserForm form, Model model, HttpServletRequest request) {
    User registerUser = new User(form.getUserId(), form.getPassword(), RoleName.USER);
    userRegisterService.register(registerUser);
    userRegisterService.authWithHttpServletRequest(request, form.getUserId(), form.getPassword());

    model.addAttribute("title", "ToDo!! | ToDo一覧");
    return "todo/todoList";
  }
}
