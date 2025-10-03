package com.modernbank.account_service.rest.controller.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class BaseRequest {

    @JsonIgnore
    private LocalDateTime time = LocalDateTime.now();

    private String token;

    @JsonIgnore
    private String userId;
}
