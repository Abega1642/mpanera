package com.mpanera.mpanera.endpoint.rest.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClerkAuthenticatedUser(
    @NotNull @NotBlank String clerkId,
    @NotNull @NotBlank String sessionId,
    @NotNull @NotBlank String email) {

  public ClerkAuthenticatedUser {
    if (clerkId == null || clerkId.isBlank())
      throw new IllegalArgumentException("clerkId is required");

    if (sessionId == null || sessionId.isBlank())
      throw new IllegalArgumentException("sessionId is required");

    if (email == null || email.isBlank()) throw new IllegalArgumentException("email is required");
  }
}
