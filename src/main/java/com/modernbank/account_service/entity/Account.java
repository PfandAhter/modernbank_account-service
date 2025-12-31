package com.modernbank.account_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.modernbank.account_service.model.enums.AccountStatus;
import com.modernbank.account_service.model.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "iban")
    private String iban;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private double balance;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private AccountStatus status;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "previous_fraud_flag")
    @Builder.Default
    private Boolean previousFraudFlag = false;

    @Column(name = "daily_transfer_limit")
    private Double dailyTransferLimit;

    @Column(name = "daily_deposit_limit")
    private Double dailyDepositLimit;

    @Column(name = "daily_withdraw_limit")
    private Double dailyWithdrawLimit;

    @Column(name = "daily_withdraw_and_deposit_limit")
    private Double dailyWithdrawAndDepositLimit;


    @Column(name = "previous_fraud_count")
    @Builder.Default
    private Integer previousFraudCount = 0;

    @Column(name = "blocked_until")
    private LocalDateTime blockedUntil;

    @Column(name = "blocked_reason")
    private String blockedReason;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Card> cards;
}