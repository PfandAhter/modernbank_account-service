package com.modernbank.account_service.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetAccountOwnerNameResponse extends BaseResponse{

    private String firstName;

    private String secondName;

    private String lastName;
}