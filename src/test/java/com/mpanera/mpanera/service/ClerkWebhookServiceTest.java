package com.mpanera.mpanera.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpanera.mpanera.endpoint.rest.controller.model.ClerkEmailAddress;
import com.mpanera.mpanera.endpoint.rest.controller.model.ClerkEmailVerification;
import com.mpanera.mpanera.endpoint.rest.controller.model.ClerkWebhookEvent;
import com.mpanera.mpanera.endpoint.rest.controller.model.ClerkWebhookUserData;
import com.mpanera.mpanera.repository.UserRepository;
import com.mpanera.mpanera.repository.model.User;
import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClerkWebhookServiceTest {

  private final HttpHeaders validHeaders = HttpHeaders.of(Map.of(), (k, v) -> true);
  private final String rawBody = "{\"type\":\"user.created\"}";
  @Mock private Webhook svix;
  @Mock private ObjectMapper objectMapper;
  @Mock private UserRepository userRepository;
  @InjectMocks private ClerkWebhookService clerkWebhookService;

  @Test
  void should_throw_verification_exception_when_signature_is_invalid()
      throws WebhookVerificationException {
    doThrow(new WebhookVerificationException("bad sig")).when(svix).verify(rawBody, validHeaders);

    assertThrows(
        WebhookVerificationException.class,
        () -> clerkWebhookService.handle(rawBody, validHeaders));
  }

  @Test
  void should_throw_deserialization_exception_when_payload_is_malformed() throws Exception {
    doNothing().when(svix).verify(rawBody, validHeaders);
    given(objectMapper.readValue(rawBody, ClerkWebhookEvent.class))
        .willThrow(new JsonProcessingException("bad json") {});

    assertThrows(IOException.class, () -> clerkWebhookService.handle(rawBody, validHeaders));
  }

  @Test
  void should_persist_user_when_user_created_event_is_received() throws Exception {
    ClerkWebhookEvent event = buildEvent("user.created");
    doNothing().when(svix).verify(rawBody, validHeaders);
    given(objectMapper.readValue(rawBody, ClerkWebhookEvent.class)).willReturn(event);
    given(userRepository.existsByClerkId("user_123")).willReturn(false);

    clerkWebhookService.handle(rawBody, validHeaders);

    verify(userRepository).save(any(User.class));
  }

  @Test
  void should_not_persist_user_when_user_already_exists_on_created_event() throws Exception {
    ClerkWebhookEvent event = buildEvent("user.created");
    doNothing().when(svix).verify(rawBody, validHeaders);
    given(objectMapper.readValue(rawBody, ClerkWebhookEvent.class)).willReturn(event);
    given(userRepository.existsByClerkId("user_123")).willReturn(true);

    clerkWebhookService.handle(rawBody, validHeaders);

    verify(userRepository, never()).save(any());
  }

  @Test
  void should_update_user_fields_when_user_updated_event_is_received() throws Exception {
    ClerkWebhookEvent event = buildEvent("user.updated");
    User existing = buildExistingUser();
    doNothing().when(svix).verify(rawBody, validHeaders);
    given(objectMapper.readValue(rawBody, ClerkWebhookEvent.class)).willReturn(event);
    given(userRepository.findByClerkId("user_123")).willReturn(Optional.of(existing));

    clerkWebhookService.handle(rawBody, validHeaders);

    assertEquals("user@example.com", existing.getEmail());
    assertEquals("John", existing.getFirstName());
    assertEquals("Doe", existing.getLastName());
  }

  @Test
  void should_not_throw_when_user_not_found_on_updated_event() throws Exception {
    ClerkWebhookEvent event = buildEvent("user.updated");
    doNothing().when(svix).verify(rawBody, validHeaders);
    given(objectMapper.readValue(rawBody, ClerkWebhookEvent.class)).willReturn(event);
    given(userRepository.findByClerkId("user_123")).willReturn(Optional.empty());

    assertDoesNotThrow(() -> clerkWebhookService.handle(rawBody, validHeaders));
    verify(userRepository, never()).save(any());
  }

  @Test
  void should_delete_user_when_user_deleted_event_is_received() throws Exception {
    ClerkWebhookEvent event = buildEvent("user.deleted");
    doNothing().when(svix).verify(rawBody, validHeaders);
    given(objectMapper.readValue(rawBody, ClerkWebhookEvent.class)).willReturn(event);

    clerkWebhookService.handle(rawBody, validHeaders);

    verify(userRepository).deleteByClerkId("user_123");
  }

  @Test
  void should_not_interact_with_repository_when_event_type_is_unknown() throws Exception {
    ClerkWebhookEvent event = new ClerkWebhookEvent("organization.created", null);
    doNothing().when(svix).verify(rawBody, validHeaders);
    given(objectMapper.readValue(rawBody, ClerkWebhookEvent.class)).willReturn(event);

    assertDoesNotThrow(() -> clerkWebhookService.handle(rawBody, validHeaders));
    verifyNoInteractions(userRepository);
  }

  private ClerkWebhookEvent buildEvent(String type) {
    var verification = new ClerkEmailVerification("verified");
    var email = new ClerkEmailAddress("email_1", "user@example.com", verification);
    var data =
        new ClerkWebhookUserData("user_123", "johndoe", "John", "Doe", "email_1", List.of(email));
    return new ClerkWebhookEvent(type, data);
  }

  private User buildExistingUser() {
    return User.builder()
        .clerkId("user_123")
        .email("old@example.com")
        .emailVerified(false)
        .username("old")
        .firstName("Old")
        .lastName("Name")
        .build();
  }
}
