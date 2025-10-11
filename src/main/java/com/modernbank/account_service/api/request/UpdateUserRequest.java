package com.modernbank.account_service.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateUserRequest extends BaseRequest{
    private String userId;
    private String email;
    private String gsm;
    private String dateOfBirth;
    private String address;
}