package com.modernbank.account_service.configuration;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import static com.modernbank.account_service.constants.HeaderKey.*;

@Component
@RequiredArgsConstructor
public class FeignInterceptor implements RequestInterceptor {

    private final HttpServletRequest request;

    @Override
    public void apply(RequestTemplate template) {
        String traceId = MDC.get("traceId");

        if (traceId != null) {
            template.header(CORRELATION_ID, traceId);
            template.header(USER_ID, request.getHeader(USER_ID));
            template.header(USER_EMAIL, request.getHeader(USER_EMAIL));
            template.header(USER_ROLE, request.getHeader(USER_ROLE));
            template.header(AUTHORIZATION_TOKEN, request.getHeader(AUTHORIZATION_TOKEN));
        }
    }
}