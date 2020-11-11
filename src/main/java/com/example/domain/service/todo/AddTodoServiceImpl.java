package com.example.domain.service.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.model.Todo;
import com.example.domain.repository.todo.TodoRepository;

/**
 * ToDoの追加に関する処理を行うサービスクラス.
 */
@Service
public class AddTodoServiceImpl implements AddTodoService {

  @Autowired
  TodoRepository repository;

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void add(Todo addTodo) {
    repository.save(addTodo);
  }
}
