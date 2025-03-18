package com.modernbank.account_service.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorCodesDTO {

    private String id;

    private String error;

    private String description;
}