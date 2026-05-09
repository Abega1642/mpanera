package com.mpanera.mpanera.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mpanera.mpanera.endpoint.rest.controller.model.ClerkAuthenticatedUser;
import com.mpanera.mpanera.service.util.ClerkClaimExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;

@Validated
class ClerkClaimExtractorTest {

  private final ClerkClaimExtractor extractor = new ClerkClaimExtractor();

  @Test
  void should_map_all_claims_to_clerk_authenticated_user() {
    Jwt jwt =
        Jwt.withTokenValue("token")
            .header("alg", "RS256")
            .subject("user_abc123")
            .claim("sid", "sess_xyz")
            .claim("email", "user@example.com")
            .build();

    ClerkAuthenticatedUser user = extractor.apply(jwt);

    assertEquals("user_abc123", user.clerkId());
    assertEquals("sess_xyz", user.sessionId());
    assertEquals("user@example.com", user.email());
  }

  @Test
  void should_throw_when_sid_claim_is_missing() {
    Jwt jwt =
        Jwt.withTokenValue("token")
            .header("alg", "RS256")
            .subject("user_abc123")
            .claim("email", "user@example.com")
            .build();

    assertThrows(IllegalArgumentException.class, () -> extractor.apply(jwt));
  }

  @Test
  void should_throw_when_email_claim_is_missing() {
    Jwt jwt =
        Jwt.withTokenValue("token")
            .header("alg", "RS256")
            .subject("user_abc123")
            .claim("sid", "sess_xyz")
            .build();

    assertThrows(IllegalArgumentException.class, () -> extractor.apply(jwt));
  }
}
