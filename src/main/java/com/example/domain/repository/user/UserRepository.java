package com.example.domain.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.model.User;

public interface UserRepository extends JpaRepository<User, String> {
}
