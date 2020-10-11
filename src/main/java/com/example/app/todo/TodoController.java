package com.example.app.todo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodoController {
  @GetMapping("/todoList")
  public String todoList(Model model) {
    model.addAttribute("title", "ToDo!! | ToDo一覧");
    return "todo/todoList";
  }

}
