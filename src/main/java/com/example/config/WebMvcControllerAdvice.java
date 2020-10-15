package com.example.config;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.example.domain.service.user.FailureAuthException;

/**
 * 全てのコントローラに適用するControllerAdvice.
 */
@ControllerAdvice
public class WebMvcControllerAdvice {
  /**
   * 入力値の空文字をnullに変換するフィルター.
   * @param dataBinder
   */
  @InitBinder
  public void initBinder(WebDataBinder dataBinder) {
    dataBinder.registerCustomEditor(String.class,
        new StringTrimmerEditor(true));
  }

  /**
   * FailureLoginException発生時にエラーページへ遷移する.
   * @param e スローされたFailureLoginException
   * @param model 連携するデータを格納
   * @return  エラー画面
   */
  @ExceptionHandler(FailureAuthException.class)
  public String handleException(FailureAuthException e, Model model) {
    model.addAttribute("errorMessage", e.getMessage());
    model.addAttribute("title", "ToDo!! | エラーが発生しました");
    return "error/error";
  }

  /**
   * DB処理エラー発生時にエラーページへ遷移する.
   * @param e スローされたDataAccessException
   * @param model 連携するデータを格納
   * @return  エラー画面
   */
  @ExceptionHandler(DataAccessException.class)
  public String handleException(DataAccessException e, Model model) {
    model.addAttribute("errorMessage", "データベース処理でエラーが発生しました");
    model.addAttribute("title", "ToDo!! | エラーが発生しました");
    return "error/error";
  }
}
