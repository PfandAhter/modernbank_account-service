package com.modernbank.account_service.api.request;

import com.modernbank.account_service.model.enums.RequestStatus;
import com.modernbank.account_service.model.enums.RequestType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class SupportRequest {
    private String trackingNumber;

    private String userId;

    private String tckn;

    private String email;

    private RequestType requestType;

    private RequestStatus status;

    private String errorCode;

    private String title;

    private String description;

    private String arguments;

    private String sourceService;

    private String endpoint;

    private String adminNotes;

    private String correlationId;

    private LocalDateTime createdAt;
}