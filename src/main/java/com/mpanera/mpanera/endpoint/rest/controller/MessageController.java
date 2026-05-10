package com.mpanera.mpanera.endpoint.rest.controller;

import com.mpanera.mpanera.endpoint.rest.controller.model.ClerkAuthenticatedUser;
import com.mpanera.mpanera.endpoint.rest.controller.model.MessagePost;
import com.mpanera.mpanera.repository.model.Message;
import com.mpanera.mpanera.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai-messages")
@RequiredArgsConstructor
public class MessageController {
  private final MessageService messageService;

  @PostMapping
  public ResponseEntity<Message> save(
      @AuthenticationPrincipal ClerkAuthenticatedUser principal,
      @RequestBody @Valid MessagePost request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(messageService.save(request));
  }

  @PostMapping("/batch")
  public ResponseEntity<List<Message>> saveBatch(
      @AuthenticationPrincipal ClerkAuthenticatedUser principal,
      @RequestBody @Valid List<MessagePost> requests) {
    return ResponseEntity.status(HttpStatus.CREATED).body(messageService.save(requests));
  }

  @GetMapping
  public ResponseEntity<List<Message>> findAll(
      @AuthenticationPrincipal ClerkAuthenticatedUser principal) {
    return ResponseEntity.ok(messageService.findByUser(principal.clerkId()));
  }

  @GetMapping("/paginated")
  public ResponseEntity<Page<Message>> findPaginated(
      @AuthenticationPrincipal ClerkAuthenticatedUser principal, Pageable pageable) {
    return ResponseEntity.ok(messageService.findByUserPaginated(principal.clerkId(), pageable));
  }

  @GetMapping("/since")
  public ResponseEntity<List<Message>> findSince(
      @AuthenticationPrincipal ClerkAuthenticatedUser principal, @RequestParam @Min(1) int hours) {
    return ResponseEntity.ok(messageService.findByUserSince(principal.clerkId(), hours));
  }

  @GetMapping("/last-month")
  public ResponseEntity<List<Message>> findLastMonth(
      @AuthenticationPrincipal ClerkAuthenticatedUser principal) {
    return ResponseEntity.ok(messageService.findByUserLastMonth(principal.clerkId()));
  }

  @GetMapping("/count")
  public ResponseEntity<Long> count(@AuthenticationPrincipal ClerkAuthenticatedUser principal) {
    return ResponseEntity.ok(messageService.countByUser(principal.clerkId()));
  }
}
