package com.mpanera.mpanera.repository;

import com.mpanera.mpanera.repository.model.Message;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findByUserClerkIdAndCreatedAtAfterOrderByCreatedAtAsc(
      String clerkId, LocalDateTime since);

  List<Message> findByUserClerkIdAndCreatedAtBetweenOrderByCreatedAtAsc(
      String userClerkId, LocalDateTime from, LocalDateTime to);

  Page<Message> findByUserClerkId(String userClerkId, Pageable pageable);

  List<Message> findByUserClerkIdOrderByCreatedAtAsc(String UserClerkId);

  @Query("SELECT COUNT(m) FROM Message m WHERE m.user.clerkId = :clerkId")
  long countConversationsByUserClerkId(@Param("clerkId") String clerkId);
}
