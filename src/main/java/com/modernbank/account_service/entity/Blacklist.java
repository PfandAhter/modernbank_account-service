package com.modernbank.account_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blacklist")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Blacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "iban", unique = true)
    private String iban;

    @Column(name = "reason")
    private String reason;

    @Column(name = "blacklisted_by")
    private String blacklistedBy;

    @Column(name = "removed_by")
    private String removedBy;

    @Column(name = "removed_date")
    private LocalDateTime removedDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}
