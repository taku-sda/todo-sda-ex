package com.example.domain.repository.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.model.Todo;

/**
 * Todoエンティティに対応したJpaRepository.
 */
public interface TodoRepository extends JpaRepository<Todo, Integer> {
}
