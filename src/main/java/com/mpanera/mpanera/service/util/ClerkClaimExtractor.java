package com.mpanera.mpanera.service.util;

import com.mpanera.mpanera.endpoint.rest.controller.model.ClerkAuthenticatedUser;
import java.util.function.Function;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class ClerkClaimExtractor implements Function<Jwt, ClerkAuthenticatedUser> {

  @Override
  public ClerkAuthenticatedUser apply(Jwt jwt) {
    String clerkId = jwt.getSubject();
    String sessionId = jwt.getClaimAsString("sid");
    String email = jwt.getClaimAsString("email");

    return new ClerkAuthenticatedUser(clerkId, sessionId, email);
  }
}
