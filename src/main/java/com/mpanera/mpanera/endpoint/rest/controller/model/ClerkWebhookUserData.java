package com.mpanera.mpanera.endpoint.rest.controller.model;

import java.util.List;

public record ClerkWebhookUserData(
    String id,
    String username,
    String firstName,
    String lastName,
    String primaryEmailAddressId,
    List<ClerkEmailAddress> emailAddresses) {

  public String resolvePrimaryEmail() {
    return emailAddresses.stream()
        .filter(e -> e.id().equals(primaryEmailAddressId))
        .map(ClerkEmailAddress::emailAddress)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Primary email not found for clerkId: " + id));
  }

  public boolean resolvePrimaryEmailVerified() {
    return emailAddresses.stream()
        .filter(e -> e.id().equals(primaryEmailAddressId))
        .map(e -> "verified".equals(e.verification().status()))
        .findFirst()
        .orElse(false);
  }
}
