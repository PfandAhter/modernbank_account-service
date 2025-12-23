package com.modernbank.account_service.api.request.account;

import com.modernbank.account_service.api.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RemoveIBANBlackListRequest extends BaseRequest {
    private String iban;
}