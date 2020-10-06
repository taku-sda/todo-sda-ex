package com.example.domain.service.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.domain.model.User;
import com.example.domain.repository.user.UserRepository;

@Service
public class TodoUserDetailsService implements UserDetailsService {
  /** Userエンティティのリポジトリインターフェイス.*/
  @Autowired
  UserRepository userRepository;

  /**
   * ${@inheritDoc}.
   */
  @Override
  public UserDetails loadUserByUsername(final String username)
      throws UsernameNotFoundException {
    Optional<User> user = userRepository.findById(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException(username + "is not found.");
    }
    return new TodoUserDetails(user.get());
  }

}
