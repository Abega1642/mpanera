package com.mpanera.mpanera.service;

import static com.mpanera.mpanera.service.UserServiceIT.generateTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mpanera.mpanera.conf.FacadeIT;
import com.mpanera.mpanera.endpoint.rest.controller.model.MAI;
import com.mpanera.mpanera.endpoint.rest.controller.model.MUser;
import com.mpanera.mpanera.endpoint.rest.controller.model.MessagePost;
import com.mpanera.mpanera.repository.MessageRepository;
import com.mpanera.mpanera.repository.UserRepository;
import com.mpanera.mpanera.repository.model.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class MessageServiceIT extends FacadeIT {

  @Autowired private MessageService subject;
  @Autowired private MessageRepository messageRepository;
  @Autowired private UserRepository userRepository;

  private User testUser;

  @BeforeEach
  void setUp() {
    messageRepository.deleteAll();
    userRepository.deleteAll();
    testUser = userRepository.save(generateTestUser());
  }

  private MessagePost generateTestMessagePost(String clerkId) {
    return new MessagePost(
        new MUser(clerkId, "What is the weather today?"),
        new MAI("I cannot check real-time weather."));
  }

  @Test
  void should_save_message_correctly() {
    var result = subject.save(generateTestMessagePost(testUser.getClerkId()));

    assertEquals(testUser.getClerkId(), result.getUser().getClerkId());
    assertEquals("What is the weather today?", result.getMessage());
    assertEquals("I cannot check real-time weather.", result.getAiResponse());
  }

  @Test
  void should_save_batch_of_messages_correctly() {
    var posts =
        List.of(
            generateTestMessagePost(testUser.getClerkId()),
            generateTestMessagePost(testUser.getClerkId()),
            generateTestMessagePost(testUser.getClerkId()));

    var result = subject.save(posts);

    assertEquals(3, result.size());
  }

  @Test
  void should_throw_when_saving_message_for_non_existing_clerk_id() {
    var post = generateTestMessagePost("clerk_nonexistent");

    assertThrows(EntityNotFoundException.class, () -> subject.save(post));
  }

  @Test
  void should_find_messages_by_user_since_last_hours() {
    subject.save(generateTestMessagePost(testUser.getClerkId()));
    subject.save(generateTestMessagePost(testUser.getClerkId()));

    var result = subject.findByUserSince(testUser.getClerkId(), 1);

    assertEquals(2, result.size());
  }

  @Test
  void should_find_messages_by_user_last_month() {
    subject.save(generateTestMessagePost(testUser.getClerkId()));

    var result = subject.findByUserLastMonth(testUser.getClerkId());

    assertEquals(1, result.size());
  }

  @Test
  void should_find_messages_by_user_paginated() {
    subject.save(generateTestMessagePost(testUser.getClerkId()));
    subject.save(generateTestMessagePost(testUser.getClerkId()));
    subject.save(generateTestMessagePost(testUser.getClerkId()));

    var result = subject.findByUserPaginated(testUser.getClerkId(), PageRequest.of(0, 2));

    assertEquals(2, result.getContent().size());
    assertEquals(3, result.getTotalElements());
    assertEquals(2, result.getTotalPages());
  }

  @Test
  void should_find_all_messages_by_user_as_list() {
    subject.save(generateTestMessagePost(testUser.getClerkId()));
    subject.save(generateTestMessagePost(testUser.getClerkId()));

    var result = subject.findByUser(testUser.getClerkId());

    assertEquals(2, result.size());
  }

  @Test
  void should_count_conversations_by_user() {
    subject.save(generateTestMessagePost(testUser.getClerkId()));
    subject.save(generateTestMessagePost(testUser.getClerkId()));
    subject.save(generateTestMessagePost(testUser.getClerkId()));

    var result = subject.countByUser(testUser.getClerkId());

    assertEquals(3, result);
  }
}
