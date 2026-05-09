package com.mpanera.mpanera.service;

import static org.owasp.encoder.Encode.forJava;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpanera.mpanera.endpoint.rest.controller.model.ClerkWebhookEvent;
import com.mpanera.mpanera.endpoint.rest.controller.model.ClerkWebhookUserData;
import com.mpanera.mpanera.repository.UserRepository;
import com.mpanera.mpanera.repository.model.User;
import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.net.http.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ClerkWebhookService {

  private final Webhook svix;
  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;

  public void handle(String rawBody, HttpHeaders headers)
      throws WebhookVerificationException, IOException {
    verifySignature(rawBody, headers);
    ClerkWebhookEvent event = deserialize(rawBody);
    dispatch(event);
  }

  private void verifySignature(String rawBody, HttpHeaders headers)
      throws WebhookVerificationException {
    svix.verify(rawBody, headers);
  }

  private ClerkWebhookEvent deserialize(String rawBody) throws IOException {
    return objectMapper.readValue(rawBody, ClerkWebhookEvent.class);
  }

  private void dispatch(ClerkWebhookEvent event) {
    switch (event.type()) {
      case "user.created" -> onCreate(event.data());
      case "user.updated" -> onUpdate(event.data());
      case "user.deleted" -> onDelete(event.data());
      default -> log.debug("Unhandled Clerk webhook event type: {}", forJava(event.type()));
    }
  }

  private void onCreate(ClerkWebhookUserData data) {
    if (userRepository.existsByClerkId(data.id())) {
      log.warn("Received user.created for already existing clerkId: {}", forJava(data.id()));
      return;
    }

    userRepository.save(
        User.builder()
            .clerkId(data.id())
            .email(data.resolvePrimaryEmail())
            .emailVerified(data.resolvePrimaryEmailVerified())
            .username(data.username())
            .firstName(data.firstName())
            .lastName(data.lastName())
            .build());

    log.info("User created from Clerk webhook, clerkId: {}", forJava(data.id()));
  }

  private void onUpdate(ClerkWebhookUserData data) {
    userRepository
        .findByClerkId(data.id())
        .ifPresentOrElse(
            user -> {
              user.setEmail(data.resolvePrimaryEmail());
              user.setEmailVerified(data.resolvePrimaryEmailVerified());
              user.setUsername(data.username());
              user.setFirstName(data.firstName());
              user.setLastName(data.lastName());
              log.info("User updated from Clerk webhook, clerkId: {}", forJava(data.id()));
            },
            () -> log.warn("Received user.updated for unknown clerkId: {}", forJava(data.id())));
  }

  private void onDelete(ClerkWebhookUserData data) {
    userRepository.deleteByClerkId(data.id());
    log.info("User deleted from Clerk webhook, clerkId: {}", data.id());
  }
}
