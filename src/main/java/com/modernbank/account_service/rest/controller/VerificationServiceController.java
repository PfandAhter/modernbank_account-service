package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.api.VerificationControllerApi;
import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.api.request.VerifyUserRequest;
import com.modernbank.account_service.api.request.verify.*;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.rest.service.OTPCodeService;
import com.modernbank.account_service.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/verification")
@RequiredArgsConstructor
public class VerificationServiceController implements VerificationControllerApi {

    private final OTPCodeService otpCodeService;

    private final UserService userService;

    @Override
    public BaseResponse verifyUser(VerifyUserRequest verifyUserRequest) {
        otpCodeService.verifyUser(verifyUserRequest);
        return new BaseResponse("User Verified Successfully");
    }

    @Override
    public BaseResponse verifyUserByAdmin(BaseRequest baseRequest) {
        return null; //TODO: Implement
    }

    @Override
    public BaseResponse unverifyUserByAdmin(BaseRequest baseRequest) {
        return null; //TODO: Implement
    }

    @Override
    public BaseResponse cancelVerification(CancelVerificationRequest request) {
        otpCodeService.deleteUserByCancelVerification(request);
        return new BaseResponse("User Cancelled Successfully");
    }

    @Override
    public BaseResponse resendVerificationEmail(ResendVerifyCodeRequest request) {
        otpCodeService.resendOTP(request);
        return new BaseResponse("Verification Email Resent Successfully");
    }

    @Override
    public BaseResponse sendPasswordResetOtp(SendPasswordResetRequest request) {
        return null;
    }

    @Override
    public BaseResponse verifyUserOtpForPasswordReset(VerifyUserOTPForPasswordResetRequest request) {
        return null;
    }

    @Override
    public BaseResponse resetUserPassword(PasswordResetRequest request) {
        return null;
    }
}