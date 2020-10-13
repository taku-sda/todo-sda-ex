package com.example.domain.service.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.model.User;
import com.example.domain.repository.user.UserRepository;

/**
 * ユーザー登録に関する処理を行うクラス.
 */
@Service
public class UserRegisterServiceImpl implements UserRegisterService {
  @Autowired
  UserRepository repository;
  
  @Override
  @Transactional(readOnly = true)
  public void confirmAvailability(String userId) {
    Optional<User> existUser = repository.findById(userId);
    if(existUser.isPresent()) {
      throw new ExistUserException("そのユーザーIDは既に使用されています。");
    }
  }

  @Override
  @Transactional
  public User register(User registerUser) {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    String hashedPassword = encoder.encode(registerUser.getPassword());
    registerUser.setPassword(hashedPassword);
    repository.save(registerUser);
    
    return registerUser;
  }
}
