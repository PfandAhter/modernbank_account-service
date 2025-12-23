package com.modernbank.account_service.api.client;

import com.modernbank.account_service.api.request.card.CardApprovalEmailRequest;
import com.modernbank.account_service.api.request.card.CardPendingEmailRequest;
import com.modernbank.account_service.api.request.card.CardRejectionEmailRequest;
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

    @PostMapping(value = "${feign.client.email-service.cardPending}")
    BaseResponse sendCardPendingNotification(@RequestBody CardPendingEmailRequest cardPendingEmailRequest);

    @PostMapping(value = "${feign.client.email-service.cardApprove}")
    BaseResponse sendCardApprovalNotification (@RequestBody CardApprovalEmailRequest cardApprovalEmailRequest);

    @PostMapping(value = "${feign.client.email-service.cardReject}")
    BaseResponse sendCardRejectionNotification (@RequestBody CardRejectionEmailRequest cardRejectionEmailRequest);
}