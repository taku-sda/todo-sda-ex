package com.example.domain.service.user;

import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.model.User;
import com.example.domain.repository.todo.TodoRepository;
import com.example.domain.repository.user.UserRepository;

/**
 * ユーザー登録解除に関する処理を行うサービスクラス.
 */
@Service
public class UserExcludeServiceImpl implements UserExcludeService {

  @Autowired
  UserRepository repository;

  @Autowired
  TodoRepository todoRepository;

  /**
   * {@inheritDoc}.
   * @throws FailureConfirmException ユーザー情報が正しくない場合
   */
  @Override
  public void confirmPassword(String userId, String password) {
    Optional<User> optionalUser = repository.findById(userId);
    if (optionalUser.isEmpty()) {
      throw new FailureConfirmException("登録されていないユーザーの登録解除です");
    }

    User existUser = optionalUser.get();
    PasswordEncoder encoder = new BCryptPasswordEncoder();

    boolean isMatch = encoder.matches(password, existUser.getPassword());
    if (!isMatch) {
      throw new FailureConfirmException("パスワードが正しくありません");
    }
  }

  /** {@inheritDoc}. */
  @Override
  @Transactional
  public void exclude(String userId) {
    repository.deleteById(userId);

    // 所有しているの全てのToDoを削除する
    todoRepository.deleteAllOwned(userId);
  }

  /**
   * {@inheritDoc}.
   * @throws FailureLogoutException ログアウトに失敗した場合
   */
  @Override
  public void logoutWithHttpServletRequest(HttpServletRequest request) {
    try {
      request.logout();
    } catch (ServletException e) {
      throw new FailureAuthException("ログアウト処理に失敗しました");
    }
  }
}
