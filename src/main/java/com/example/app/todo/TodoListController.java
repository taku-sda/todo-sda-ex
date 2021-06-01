package com.example.app.todo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.model.Todo;
import com.example.domain.service.todo.TodoListService;
import com.example.domain.service.user.TodoUserDetails;

/**
 * ToDo一覧表示機能のコントローラ.
 */
@Controller
public class TodoListController {

  @Autowired
  TodoListService todoListService;

  /**
   * Todo一覧画面へ遷移する. リクエストパラメータにより、表示内容を切り替える.
   * @param listType 一覧の種類
   * @param sort ソートの種類
   * @param order ソートの順番
   * @param userDetails ログイン中のユーザー情報
   * @param model 連携するデータを格 納
   * @return Todo一覧画面
   */
  @GetMapping("/todoList")
  public String todoList(@RequestParam(defaultValue = "normal") String listType,
      @RequestParam(defaultValue = "deadline") String sort,
      @RequestParam(defaultValue = "ASC") String order,
      @AuthenticationPrincipal TodoUserDetails userDetails, Model model) {

    String authUserId = userDetails.getUser().getUserId();
    List<Todo> displayList = null;
    try {
      displayList = todoListService.getDisplayList(authUserId, listType, sort, order);
    } catch (IllegalArgumentException e) {
      throw new IllegalOperationException(e.getMessage());
    }

    List<Todo> todayList = todoListService.getTodayList(authUserId);
    List<Todo> expiredList = todoListService.getExpiredList(authUserId);

    model.addAttribute("displayList", displayList);
    model.addAttribute("todayList", todayList);
    model.addAttribute("expiredList", expiredList);
    model.addAttribute("listType", listType);
    model.addAttribute("sort", sort);
    model.addAttribute("order", order);
    return "todo/todoList";
  }
}
