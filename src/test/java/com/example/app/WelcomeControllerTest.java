package com.example.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.domain.service.user.TodoUserDetailsService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WelcomeController.class)
@DisplayName("WelcomeControllerの単体テスト")
class WelcomeControllerTest {
  @Autowired
  MockMvc mockMvc;
  
  @MockBean
  TodoUserDetailsService  userDetailsService;

  @Test
  @DisplayName("トップページに遷移する")
  void transitionIndex() throws Exception {
    mockMvc.perform(get("/"))
      .andExpect(status().isOk())
      .andExpect(view().name("index"));
  }
}
