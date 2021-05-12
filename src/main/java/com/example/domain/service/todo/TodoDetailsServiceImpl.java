package com.example.domain.service.todo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.model.Todo;
import com.example.domain.repository.todo.TodoRepository;

/**
 * 既存のToDoの登録内容の詳細や変更に関する処理を行うサービスクラス.
 */
@Service
public class TodoDetailsServiceImpl implements TodoDetailsService {

  @Autowired
  TodoRepository repository;

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public Todo getTodo(Integer todoId) {
    Optional<Todo> optionalTodo = repository.findById(todoId);
    if (optionalTodo.isEmpty()) {
      throw new IllegalArgumentException("指定されたTodoは存在しません。");
    }

    return optionalTodo.get();
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void updateDetails(Integer todoId, String title, LocalDateTime deadline, Integer priority,
      String memo) {
    Todo updateTodo = getTodo(todoId);
    updateTodo.setTitle(title);
    updateTodo.setDeadline(deadline);
    updateTodo.setPriority(priority);
    updateTodo.setMemo(memo);
    updateTodo.setLastUpdate(LocalDateTime.now(ZoneId.of("Asia/Tokyo")));
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void deleteTodo(Integer todoId) {
    repository.deleteById(todoId);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public int deleteAllCompletedTodo(String userId) {
    return repository.deleteAllCompleted(userId);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public int deleteAllExpiredTodo(String userId) {
    return repository.deleteAllExpired(userId);
  }

}
