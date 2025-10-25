package com.modernbank.account_service.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyUserRequest {

    @JsonIgnore
    private LocalDateTime time = LocalDateTime.now();

    @JsonIgnore
    private String type;

    private String userEmail;
    private String otp;
}