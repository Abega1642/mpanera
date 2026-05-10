package com.mpanera.mpanera.service;

import com.mpanera.mpanera.endpoint.rest.controller.model.MessagePost;
import com.mpanera.mpanera.repository.MessageRepository;
import com.mpanera.mpanera.repository.model.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class MessageService {
  private final MessageRepository messageRepository;
  private final UserService userService;

  public Message save(@NotNull @Valid MessagePost messagePost) {
    log.info("Saving message for clerkId={}", messagePost.userRequest().clerkId());

    var subjectUser = userService.findByClerkId(messagePost.userRequest().clerkId());

    Message subjectMessage =
        Message.builder()
            .user(subjectUser)
            .message(messagePost.userRequest().message())
            .aiResponse(messagePost.aiResponse().response())
            .build();

    return messageRepository.save(subjectMessage);
  }

  public List<Message> save(@NotNull List<@NotNull @Valid MessagePost> messagePosts) {
    log.info("Saving {} messages in batch", messagePosts.size());
    return messagePosts.stream().map(this::save).toList();
  }

  public List<Message> findByUserSince(@NotNull String clerkId, int hours) {
    log.info("Fetching messages for clerkId={} since last {} hour(s)", clerkId, hours);
    return messageRepository.findByUserClerkIdAndCreatedAtAfterOrderByCreatedAtAsc(
        clerkId, LocalDateTime.now().minusHours(hours));
  }

  public List<Message> findByUserLastMonth(@NotNull String clerkId) {
    log.info("Fetching messages for clerkId={} for the last month", clerkId);
    return messageRepository.findByUserClerkIdAndCreatedAtBetweenOrderByCreatedAtAsc(
        clerkId, LocalDateTime.now().minusMonths(1), LocalDateTime.now());
  }

  public Page<Message> findByUserPaginated(@NotNull String clerkId, @NotNull Pageable pageable) {
    log.info(
        "Fetching paginated messages for clerkId={}, page={}, size={}",
        clerkId,
        pageable.getPageNumber(),
        pageable.getPageSize());
    return messageRepository.findByUserClerkId(clerkId, pageable);
  }

  public List<Message> findByUser(@NotNull String clerkId) {
    log.info("Fetching all messages for clerkId={}", clerkId);
    return messageRepository.findByUserClerkIdOrderByCreatedAtAsc(clerkId);
  }

  public long countByUser(@NotNull String clerkId) {
    log.info("Counting conversations for clerkId={}", clerkId);
    return messageRepository.countConversationsByUserClerkId(clerkId);
  }
}
