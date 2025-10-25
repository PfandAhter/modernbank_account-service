package com.modernbank.account_service.api.client;

import com.modernbank.account_service.api.request.VerifyUserRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "email-service", url = "${feign.client.email-service.url}")
public interface EmailServiceClient {

    @PostMapping(value = "${feign.client.email-service.verifyUser}")
    BaseResponse sendVerificationEmail(@RequestBody VerifyUserRequest request);

    @PostMapping(value = "${feign.client.email-service.passwordReset}")
    BaseResponse sendPasswordResetEmail(@RequestBody VerifyUserRequest request);
}