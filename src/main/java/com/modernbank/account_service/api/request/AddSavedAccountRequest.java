package com.modernbank.account_service.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AddSavedAccountRequest extends BaseRequest{
    private String nickname;

    private String accountIBAN;

    private String firstName;

    private String secondName;

    private String lastName;
}