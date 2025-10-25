package com.modernbank.account_service.api;

import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.api.request.VerifyUserRequest;
import com.modernbank.account_service.api.request.verify.*;
import com.modernbank.account_service.api.response.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface VerificationControllerApi {

    @PostMapping(path = "/user/verify")
    BaseResponse verifyUser(@RequestBody VerifyUserRequest verifyUserRequest);

    @PostMapping(path = "/user/verify/cancel")
    BaseResponse cancelVerification(@RequestBody CancelVerificationRequest request);

    @PostMapping(path = "/user/verify/resend")
    BaseResponse resendVerificationEmail(@RequestBody ResendVerifyCodeRequest request);


    @PostMapping(path = "/user/password/send-reset-otp")
    BaseResponse sendPasswordResetOtp(@RequestBody SendPasswordResetRequest request);

    @PostMapping(path = "/user/password/verify-user-otp")
    BaseResponse verifyUserOtpForPasswordReset(@RequestBody VerifyUserOTPForPasswordResetRequest request);

    @PostMapping(path = "/user/password/reset")
    BaseResponse resetUserPassword(@RequestBody PasswordResetRequest request);


    @PostMapping(path = "/verify-by-admin")
    BaseResponse verifyUserByAdmin(@RequestBody BaseRequest baseRequest); //TODO: Burada birden fazla userRole gelmesine gore admin olup olmadigini kontrol et..

    @PostMapping(path = "/unverify-by-admin")
    BaseResponse unverifyUserByAdmin(@RequestBody BaseRequest baseRequest); //TODO: Burada birden fazla userRole gelmesine gore admin olup olmadigini kontrol et..
}