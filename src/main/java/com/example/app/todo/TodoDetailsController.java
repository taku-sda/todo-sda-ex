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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.model.Todo;
import com.example.domain.service.todo.TodoDetailsService;
import com.example.domain.service.user.TodoUserDetails;

/**
 * 既存のToDoの登録内容の詳細や変更に関する処理を行うコントローラ.
 */
@Controller
@RequestMapping("/todoDetails")
public class TodoDetailsController {

  @Autowired
  TodoDetailsService todoDetailsService;

  /**
   * 指定されたIDのToDoの詳細画面に遷移する.
   * @param todoId 詳細を表示するToDoのID
   * @param userDetails ログイン中のユーザー情報
   * @param model 連携するデータを格納
   * @return 指定されたIDのToDoの詳細画面
   */
  @GetMapping
  String todoForm(@RequestParam int todoId, @AuthenticationPrincipal TodoUserDetails userDetails,
      Model model) {

    Todo todoDetails = todoDetailsService.getTodo(todoId);

    // ToDoの作成者以外からのアクセスの場合は例外をスロー
    if (!(todoDetails.getUser().getUserId().equals(userDetails.getUser().getUserId()))) {
      throw new IllegalOperationException("このToDoの詳細を閲覧する権限がありません");
    }

    TodoForm todoForm = new TodoForm();
    todoForm.setTitle(todoDetails.getTitle());
    todoForm.setDeadlineStr(convertToString(todoDetails.getDeadline()));
    todoForm.setPriority(todoDetails.getPriority());
    todoForm.setMemo(todoDetails.getMemo());

    model.addAttribute("todoForm", todoForm);
    model.addAttribute("todoId", todoId);
    model.addAttribute("completed", todoDetails.isCompleted());
    model.addAttribute("lastUpdate", todoDetails.getLastUpdate());

    return "todo/todoDetails";
  }

  /**
   * ToDoの登録内容の更新を行う.
   * @param form ToDo更新フォームの入力内容
   * @param result バリデーションの結果
   * @param todoId 更新するToDoのID
   * @param model 連携するデータを格納
   * @return ToDo一覧画面
   */
  @PostMapping
  String updateDetails(@Validated TodoForm form, BindingResult result, @RequestParam int todoId,
      Model model) {
    if (result.hasErrors()) {
      return "todo/todoDetails";
    }

    todoDetailsService.updateDetails(todoId, form.getTitle(), convertToLocalDateTime(form
        .getDeadlineStr()), form.getPriority(), form.getMemo());

    return "redirect:/todoList";
  }

  /**
   * LocalDateTimeを日時を表す文字列に変換する.
   * @param ldt LocalDateTime
   * @return 変換後の日時を表す文字列
   */
  private String convertToString(LocalDateTime ldt) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    return ldt.format(formatter);
  }

  /**
   * 日時を表す文字列をLocalDateTimeに変換する.
   * @param ldtStr 日付を表す文字列
   * @return 変換後のLocalDateTime
   */
  private LocalDateTime convertToLocalDateTime(String ldtStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    return LocalDateTime.parse(ldtStr, formatter);
  }
}