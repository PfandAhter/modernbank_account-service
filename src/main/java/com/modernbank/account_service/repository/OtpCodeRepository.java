package com.modernbank.account_service.repository;

import com.modernbank.account_service.entity.OTPCode;
import com.modernbank.account_service.model.enums.OTPType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OTPCode, Long> {

    boolean existsByOtp(String otp);

    @Query("SELECT o FROM OTPCode o WHERE o.userEmail =:userEmail AND o.otp =:otp AND o.otpType=:otpType AND o.used=:used ")
    Optional<OTPCode> findByUserEmailAndOtp (String userEmail, String otp, Boolean used, OTPType otpType);

    List<OTPCode> findAllByExpiryTimeBeforeAndUsedFalse(LocalDateTime expiryTime);

    @Query("SELECT o FROM OTPCode o WHERE o.userEmail=:email AND o.otpType =:otpType AND o.used =:used ORDER BY o.expiryTime DESC")
    List<OTPCode> findAllByUserEmailAndUsedFalse(String email, Boolean used, OTPType otpType);
}