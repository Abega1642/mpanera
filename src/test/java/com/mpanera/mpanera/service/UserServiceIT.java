package com.mpanera.mpanera.service;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mpanera.mpanera.conf.FacadeIT;
import com.mpanera.mpanera.endpoint.rest.controller.model.UserUpdateRequest;
import com.mpanera.mpanera.repository.UserRepository;
import com.mpanera.mpanera.repository.model.User;
import com.mpanera.mpanera.repository.model.UserRole;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceIT extends FacadeIT {
  @Autowired private UserService subject;
  @Autowired private UserRepository repository;

  public static User generateTestUser() {
    return User.builder()
        .clerkId("clerk_" + randomUUID())
        .email(format("user-%s@example.com", randomUUID()))
        .emailVerified(true)
        .username("testuser")
        .firstName("John")
        .lastName("Doe")
        .role(UserRole.CLIENT)
        .onBoardingComplete(false)
        .build();
  }

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void should_find_user_by_id() {
    var saved = repository.save(generateTestUser());

    var result = subject.findById(saved.getId());

    assertEquals(saved.getId(), result.getId());
    assertEquals(saved.getClerkId(), result.getClerkId());
    assertEquals(saved.getEmail(), result.getEmail());
  }

  @Test
  void should_throw_when_user_not_found_by_id() {
    assertThrows(EntityNotFoundException.class, () -> subject.findById(randomUUID()));
  }

  @Test
  void should_update_user_correctly() {
    var saved = repository.save(generateTestUser());
    var request = new UserUpdateRequest("Antananarivo", "Analamanga", true);

    var result = subject.update(saved.getId(), request);

    assertEquals("Antananarivo", result.getCity());
    assertEquals("Analamanga", result.getDistrict());
    assertTrue(result.isOnBoardingComplete());
  }

  @Test
  void should_throw_when_updating_non_existing_user() {
    var request = new UserUpdateRequest("Antananarivo", "Analamanga", true);

    assertThrows(EntityNotFoundException.class, () -> subject.update(randomUUID(), request));
  }
}
