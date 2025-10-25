package com.modernbank.account_service.api.request.verify;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class VerifyUserOTPForPasswordResetRequest extends ResendVerifyCodeRequest{
    private String otp;
}