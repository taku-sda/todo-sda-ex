package com.example.app.todo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodoListController {
  @GetMapping("/todoList")
  public String todoList(Model model) {
    return "todo/todoList";
  }

}
