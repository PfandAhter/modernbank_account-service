package com.modernbank.account_service.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserInfoRequest extends UpdateUserRequest{
    private String tckn;
}