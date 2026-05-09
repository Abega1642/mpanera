package com.mpanera.mpanera.repository.model;

import static com.mpanera.mpanera.repository.model.enums.NotificationStatus.SENT;

import com.mpanera.mpanera.repository.model.enums.NotificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "notifications")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "service_request_id", nullable = false)
  private ServiceRequest serviceRequest;

  @ManyToOne(optional = false)
  @JoinColumn(name = "provider_id", nullable = false)
  private Provider provider;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(nullable = false)
  @Builder.Default
  private NotificationStatus status = SENT;

  @Column(name = "sent_at", insertable = false, updatable = false)
  private LocalDateTime sentAt;

  @Column(name = "viewed_at")
  private LocalDateTime viewedAt;
}
