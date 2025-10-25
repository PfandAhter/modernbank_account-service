package com.modernbank.account_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "saved_accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SavedAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "accountIBAN")
    private String accountIBAN;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "secondName")
    private String secondName;

    @Column(name = "lastName")
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}