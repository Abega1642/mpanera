package com.mpanera.mpanera.repository.model;

import com.mpanera.mpanera.repository.model.enums.OfferStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
@Table(name = "offers")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class Offer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "notification_id", nullable = false)
  private Notification notification;

  @ManyToOne(optional = false)
  @JoinColumn(name = "service_request_id", nullable = false)
  private ServiceRequest serviceRequest;

  @ManyToOne(optional = false)
  @JoinColumn(name = "provider_id", nullable = false)
  private Provider provider;

  @Column(name = "proposed_price", nullable = false, precision = 65, scale = 30)
  private BigDecimal proposedPrice;

  @Column(columnDefinition = "TEXT")
  private String message;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(nullable = false)
  @Builder.Default
  private OfferStatus status = OfferStatus.PENDING;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;
}
