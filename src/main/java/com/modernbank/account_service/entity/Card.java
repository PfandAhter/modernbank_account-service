package com.modernbank.account_service.entity;

import com.modernbank.account_service.model.enums.CardNetwork;
import com.modernbank.account_service.model.enums.CardStatus;
import com.modernbank.account_service.model.enums.CardType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "card")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "raw_cvv_encrypted")
    private String rawCvvEncrypted;

    @Column(name = "last_four_digits", length = 4)
    private String lastFourDigits;

    @Column(name = "card_number_encrypted")
    private String cardNumberEncrypted;

    @Column(name = "card_number_hash", unique = true)
    private String cardNumberHash;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "card_type", length = 50)
    @Enumerated(EnumType.STRING)
    private CardType type;

    @Column(name = "status", length = 50)
    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @Column(name = "network", length = 50)
    @Enumerated(EnumType.STRING)
    private CardNetwork network;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "limit_amount")
    private Double limitAmount;

    @Column(name = "available_amount")
    private Double availableAmount;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "is_email_notified")
    private Boolean isEmailNotified;
}