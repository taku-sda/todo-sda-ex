package com.example.domain.repository.todo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.example.domain.model.Todo;

public class TodoRepositoryImpl implements TodoRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  /** {@inheritDoc} */
  @Override
  public List<Todo> getNormalListAnySorted(String userId, String sort, String order) {
    String jpql = "SELECT t FROM Todo t WHERE t.user.userId = :userId AND t.deadline >= CURRENT_TIMESTAMP "
        + "AND t.completed = FALSE ORDER BY" + " " + sort + " " + order;
    TypedQuery<Todo> query = entityManager.createQuery(jpql, Todo.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }

  /** {@inheritDoc} */
  @Override
  public List<Todo> getCompletedListAnySorted(String userId, String sort, String order) {
    String jpql = "SELECT t FROM Todo t WHERE t.user.userId = :userId AND t.completed = TRUE "
        + "ORDER BY" + " " + sort + " " + order;
    TypedQuery<Todo> query = entityManager.createQuery(jpql, Todo.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }

  /** {@inheritDoc} */
  @Override
  public List<Todo> getExpiredListAnySorted(String userId, String sort, String order) {
    String jpql = "SELECT t FROM Todo t WHERE t.user.userId = :userId AND t.deadline < CURRENT_TIMESTAMP "
        + "AND t.completed = FALSE ORDER BY" + " " + sort + " " + order;
    TypedQuery<Todo> query = entityManager.createQuery(jpql, Todo.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }
}
