package com.example.domain.service.todo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.model.Todo;
import com.example.domain.repository.todo.TodoRepository;

/**
 * oDoの一覧表示に関する処理を行うサービスクラス.
 */
@Service
public class TodoListServiceImpl implements TodoListService {

  @Autowired
  TodoRepository repository;

  /**
   * {@inheritDoc}
   * @throws IllegalArgumentException ToDo一覧取得の条件が指定されたものでない場合
   */
  @Override
  public List<Todo> getDisplayList(String userId, String listType, String sort, String order) {
    if (!(listType.matches("normal|completed|expired") && sort.matches(
        "deadline|priority|lastUpdate") && order.matches("ASC|DESC"))) {
      throw new IllegalArgumentException("ToDo一覧取得の条件が不正です");
    }

    List<Todo> displayList = new ArrayList<>();
    switch (listType) {
    case "normal":
      displayList = repository.getNormalListAnySorted(userId, sort, order);
      break;
    case "completed":
      displayList = repository.getCompletedListAnySorted(userId, sort, order);
      break;
    case "expired":
      displayList = repository.getExpiredListAnySorted(userId, sort, order);
      break;
    default:
    }

    return displayList;
  }

  /** {@inheritDoc} */
  @Override
  public List<Todo> getTodayList(String userId) {
    return repository.getTodayList(userId);
  }

  /** {@inheritDoc} */
  @Override
  public List<Todo> getExpiredList(String userId) {
    return repository.getExpiredList(userId);
  }
}
