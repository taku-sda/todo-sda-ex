package com.example.app.todo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.model.Todo;
import com.example.domain.service.todo.AddTodoService;
import com.example.domain.service.user.TodoUserDetails;

@Controller
@RequestMapping("/addTodo")
public class AddTodoController {

  @Autowired
  AddTodoService addTodoService;

  @ModelAttribute
  public TodoForm setUpForm() {
    return new TodoForm();
  }

  /**
   * ToDo追加フォーム画面へ遷移する.
   * @param model 連携するデータを格納
   * @return  ToDo追加フォーム画面
   */
  @GetMapping
  public String addTodoForm(Model model) {
    return "todo/addTodo";
  }

  @PostMapping
  public String add(@Validated TodoForm form, BindingResult result, 
      @AuthenticationPrincipal TodoUserDetails userDetails, Model model) {
    if(result.hasErrors()) {
      return addTodoForm(model);
    }

    Todo addTodo =createAddTodo(form, userDetails);
    addTodoService.add(addTodo);

    return "todo/todoList";
  }

  /**
   * TodoFormクラスと認証情報を基に、追加するTodoエンティティを作成する.
   * @param form  Todo追加フォームの入力内容
   * @param userDetails ユーザーの認証情報
   * @return  追加するTodoエンティティ
   */
  private Todo createAddTodo(TodoForm form, TodoUserDetails userDetails) {
    Todo addTodo = new Todo();
    addTodo.setUser(userDetails.getUser());
    addTodo.setTitle(form.getTitle());
    addTodo.setDeadline(convertToLocalDateTime(form.getDeadlineStr()));
    addTodo.setPriority(form.getPriority());
    addTodo.setMemo(form.getMemo());
    addTodo.setCompleted(false);  //未完了状態
    addTodo.setLastUpdate(LocalDateTime.now()); //現在の日時

    return addTodo;
  }

  /**
   * 日時を表す文字列をLocalDateTimeに変換する
   * @param deadlineStr
   * @return
   */
  private LocalDateTime convertToLocalDateTime(String deadlineStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    return LocalDateTime.parse(deadlineStr, formatter);
  }
}
