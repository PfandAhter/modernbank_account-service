package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.api.client.EmailServiceClient;
import com.modernbank.account_service.api.request.VerifyUserRequest;
import com.modernbank.account_service.api.request.verify.CancelVerificationRequest;
import com.modernbank.account_service.api.request.verify.ResendVerifyCodeRequest;
import com.modernbank.account_service.entity.OTPCode;
import com.modernbank.account_service.entity.User;
import com.modernbank.account_service.exception.BusinessException;
import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.enums.OTPType;
import com.modernbank.account_service.repository.OtpCodeRepository;
import com.modernbank.account_service.repository.UserRepository;
import com.modernbank.account_service.rest.service.OTPCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.modernbank.account_service.constants.ErrorCodeConstants.OTP_EXPIRED;
import static com.modernbank.account_service.constants.ErrorCodeConstants.OTP_ALREADY_USED;
import static com.modernbank.account_service.constants.ErrorCodeConstants.OTP_NOT_FOUND_OR_INVALID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OTPCodeServiceImpl implements OTPCodeService {

    private final OtpCodeRepository otpCodeRepository;

    private final EmailServiceClient emailServiceClient;

    private final UserRepository userRepository;

    @Override
    public void verifyUser(VerifyUserRequest verifyUserRequest) {
        OTPCode otpCode = otpCodeRepository.findByUserEmailAndOtp(
                        verifyUserRequest.getUserEmail(), verifyUserRequest.getOtp(),false,OTPType.CREATION)
                .orElseThrow(() -> new NotFoundException(OTP_NOT_FOUND_OR_INVALID));

        if (otpCode.getUsed()) {
            throw new BusinessException(OTP_ALREADY_USED);
        }

        if (verifyUserRequest.getTime().isAfter(otpCode.getExpiryTime())) {
            throw new BusinessException(OTP_EXPIRED);
        }

        User user = userRepository.findByEmail(verifyUserRequest.getUserEmail())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + verifyUserRequest.getUserEmail())); // TODO: Burada ki gibi dynamic value olanlari baska bir sekilde handle edeyim.

        user.setEnabled(true);
        user.setUpdatedDate(LocalDateTime.now());

        otpCode.setUsed(true);
        otpCode.setUpdatedAt(LocalDateTime.now());

        otpCodeRepository.save(otpCode);
        userRepository.save(user);
    }

    @Override
    public void deleteUserByCancelVerification(CancelVerificationRequest request) {
        log.info("Deleting user and associated OTPs for email: {}", request.getEmail());
        /*List<OTPCode> otps = otpCodeRepository.findAllByUserEmailAndUsedFalse(request.getEmail(),false,OTPType.CREATION)
                .orElseThrow(() -> new NotFoundException("No previous OTPs found for email: " + request.getEmail())); // TODO: Burada ki gibi dynamic value olanlari baska bir sekilde handle edeyim.
        */

        List<OTPCode> otps = otpCodeRepository.findAllByUserEmailAndUsedFalse(request.getEmail(),false,OTPType.CREATION);

        if(otps.isEmpty()) {
            return;
        }

        for (OTPCode otp : otps) {
            otpCodeRepository.delete(otp);
        }
        userRepository.findByEmail(request.getEmail()).ifPresent(userRepository::delete);
    }

    @Override
    public void resendOTP(ResendVerifyCodeRequest request) {
        createUserVerificationOTP(request.getEmail(),"CREATION");
    }

    @Override
    public void createUserVerificationOTP(String email, String type) {
        invalidatePreviousOtps(email);
        String otpCode;
        do {
            otpCode = String.format("%06d", (int) (Math.random() * 1_000_000));// Java.security.SecureRandom use...
        } while (otpCodeRepository.existsByOtp(otpCode));

        OTPCode otp = OTPCode.builder()
                .otp(otpCode)
                .userEmail(email)
                .createdAt(LocalDateTime.now())
                .otpType(OTPType.valueOf(type))
                .updatedAt(LocalDateTime.now())
                .used(false)
                .expiryTime(LocalDateTime.now().plusMinutes(10))
                .build();

        otpCodeRepository.save(otp);


        if(type.equals("CREATION")){ //TODO: use otp.getOtpType() == OTPType.CREATION
            emailServiceClient.sendVerificationEmail(VerifyUserRequest.builder()
                    .otp(otpCode)
                    .userEmail(email)
                    .type(type)
                    .build());
        }else if(type.equals("PASSWORD_RESET")){
            emailServiceClient.sendPasswordResetEmail(VerifyUserRequest.builder()
                    .otp(otpCode)
                    .userEmail(email)
                    .type(type)
                    .build());
        }

        log.info("Email send to: {}", email);
    }

    private void invalidatePreviousOtps(String email) {
        List<OTPCode> previousOTPS = otpCodeRepository.findAllByUserEmailAndUsedFalse(email,false,OTPType.CREATION);

        if(previousOTPS.isEmpty()) {
            return;
        }

        for (OTPCode otp : previousOTPS) {
            otp.setUsed(true);
            otp.setUpdatedAt(LocalDateTime.now());
            otpCodeRepository.save(otp);
        }
    }
}