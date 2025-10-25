package com.modernbank.account_service.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetAccountDetailsRequest extends BaseRequest{
    private String accountId;
}