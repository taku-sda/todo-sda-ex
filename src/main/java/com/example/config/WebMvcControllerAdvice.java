package com.example.config;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

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
    dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }
}