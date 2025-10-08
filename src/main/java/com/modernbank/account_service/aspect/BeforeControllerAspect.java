package com.modernbank.account_service.aspect;


import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.rest.service.IHeaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@Slf4j
@RequiredArgsConstructor

public class BeforeControllerAspect {

    private final IHeaderService headerService;

    @Before(value = "execution(* com.modernbank.atm_reporting_service.websocket.controller.ATMServiceController.*(..))")
    public void setTokenBeforeController(JoinPoint joinPoint){
        Object[] parameters = joinPoint.getArgs();
        for(Object param : parameters){
            if(param instanceof BaseRequest baseRequest){
                String token = headerService.extractToken();
                String userId = headerService.extractUserId();
                String userRole = headerService.extractUserRole();
                baseRequest.setToken(token);
                baseRequest.setUserId(userId);
            }
        }
    }
}