package com.mpanera.mpanera.endpoint.rest.controller;

import static com.mpanera.mpanera.service.UserServiceIT.generateTestUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mpanera.mpanera.conf.FacadeIT;
import com.mpanera.mpanera.endpoint.rest.controller.model.ClerkAuthenticatedUser;
import com.mpanera.mpanera.repository.MessageRepository;
import com.mpanera.mpanera.repository.UserRepository;
import com.mpanera.mpanera.repository.model.Message;
import com.mpanera.mpanera.repository.model.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Transactional
class MessageControllerIT extends FacadeIT {

  @Autowired private MockMvc mvc;
  @Autowired private UserRepository userRepository;
  @Autowired private MessageRepository messageRepository;

  private User testUser;

  public static RequestPostProcessor mockClerkUser(User user) {
    return SecurityMockMvcRequestPostProcessors.authentication(
        new UsernamePasswordAuthenticationToken(
            new ClerkAuthenticatedUser(user.getClerkId(), "session_test", user.getEmail()),
            null,
            List.of()));
  }

  @BeforeEach
  void setUp(WebApplicationContext context) {
    mvc =
        MockMvcBuilders.webAppContextSetup(context)
            .defaultRequest(MockMvcRequestBuilders.post("/**").with(csrf()))
            .apply(springSecurity())
            .build();
    messageRepository.deleteAll();
    userRepository.deleteAll();
    testUser = userRepository.save(generateTestUser());
  }

  private void saveMessages(int count) {
    for (int i = 0; i < count; i++) {
      messageRepository.saveAndFlush(
          Message.builder()
              .user(testUser)
              .message("Question " + i)
              .aiResponse("Answer " + i)
              .build());
    }
  }

  private String singleMessageBody() {
    return """
    {
      "user_request": {
        "clerk_id": "%s",
        "message": "What is the weather today?"
      },
      "ai_response": {
        "response": "I cannot check real-time weather."
      }
    }
    """
        .formatted(testUser.getClerkId());
  }

  private String batchMessageBody() {
    return """
    [
      {
        "user_request": {"clerk_id": "%s", "message": "Q1"},
        "ai_response": {"response": "A1"}
      },
      {
        "user_request": {"clerk_id": "%s", "message": "Q2"},
        "ai_response": {"response": "A2"}
      }
    ]
    """
        .formatted(testUser.getClerkId(), testUser.getClerkId());
  }

  @Test
  void should_save_message_and_return_201() throws Exception {
    mvc.perform(
            post("/ai-messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(singleMessageBody())
                .with(mockClerkUser(testUser)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("What is the weather today?"))
        .andExpect(jsonPath("$.ai_response").value("I cannot check real-time weather."));
  }

  @Test
  void should_save_batch_and_return_201() throws Exception {
    mvc.perform(
            post("/ai-messages/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(batchMessageBody())
                .with(mockClerkUser(testUser)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void should_find_all_messages_for_authenticated_user() throws Exception {
    saveMessages(3);

    mvc.perform(get("/ai-messages").with(mockClerkUser(testUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3));
  }

  @Test
  void should_find_paginated_messages() throws Exception {
    saveMessages(5);

    mvc.perform(
            get("/ai-messages/paginated")
                .param("page", "0")
                .param("size", "3")
                .with(mockClerkUser(testUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(3))
        .andExpect(jsonPath("$.total_elements").value(5))
        .andExpect(jsonPath("$.total_pages").value(2));
  }

  @Test
  void should_find_messages_since_last_hour() throws Exception {
    saveMessages(2);

    mvc.perform(get("/ai-messages/since").param("hours", "1").with(mockClerkUser(testUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void should_find_messages_from_last_month() throws Exception {
    saveMessages(2);

    mvc.perform(get("/ai-messages/last-month").with(mockClerkUser(testUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void should_count_conversations_for_authenticated_user() throws Exception {
    saveMessages(4);

    mvc.perform(get("/ai-messages/count").with(mockClerkUser(testUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(4));
  }
}
