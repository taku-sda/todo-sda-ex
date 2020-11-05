package com.example.domain.service.todo;

import com.example.domain.model.Todo;

public interface AddTodoService {

  /**
   * ToDoの追加処理を行う.
   * @param addTodo 追加するToDo
   */
  void add(Todo addTodo);
}
