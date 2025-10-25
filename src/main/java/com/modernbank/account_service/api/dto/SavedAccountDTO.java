package com.modernbank.account_service.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SavedAccountDTO {
    private String id;

    private String nickname;

    private String accountIBAN;

    private String firstName;

    private String secondName;

    private String lastName;
}