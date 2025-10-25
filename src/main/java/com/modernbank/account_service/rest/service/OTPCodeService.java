package com.modernbank.account_service.rest.service;

import com.modernbank.account_service.api.request.VerifyUserRequest;
import com.modernbank.account_service.api.request.verify.CancelVerificationRequest;
import com.modernbank.account_service.api.request.verify.ResendVerifyCodeRequest;

public interface OTPCodeService {
    void verifyUser(VerifyUserRequest verifyUserRequest);

    void deleteUserByCancelVerification(CancelVerificationRequest request);

    void resendOTP(ResendVerifyCodeRequest request);

    void createUserVerificationOTP(String email, String type);
}
