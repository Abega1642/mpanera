package com.mpanera.mpanera.endpoint.rest.controller;

import com.mpanera.mpanera.service.ClerkWebhookService;
import com.mpanera.mpanera.service.util.ServletHttpHeaderExtractor;
import com.svix.exceptions.WebhookVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class ClerkWebhookController {

  private final ClerkWebhookService clerkWebhookService;
  private final ServletHttpHeaderExtractor headerExtractor;

  @PostMapping("/clerk")
  public ResponseEntity<Void> handle(HttpServletRequest request, @RequestBody String rawBody)
      throws WebhookVerificationException, IOException {
    clerkWebhookService.handle(rawBody, headerExtractor.apply(request));

    return ResponseEntity.ok().build();
  }
}
