package com.modernbank.account_service.entity;

import com.modernbank.account_service.model.enums.OTPType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_code")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OTPCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userEmail", nullable = false)
    private String userEmail;

    @Column(name = "otp", nullable = false)
    private String otp;

    @Column(name = "otpType", nullable = false)
    private OTPType otpType;

    @Column(name = "expiryTime", nullable = false)
    private LocalDateTime expiryTime;

    @Column(name = "used", nullable = false)
    private Boolean used;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;
}