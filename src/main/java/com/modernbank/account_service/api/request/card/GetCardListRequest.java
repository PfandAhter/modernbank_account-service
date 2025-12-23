package com.modernbank.account_service.api.request.card;

import com.modernbank.account_service.api.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetCardListRequest extends BaseRequest {
    private String accountId;
}