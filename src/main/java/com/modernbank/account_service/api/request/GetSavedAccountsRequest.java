package com.modernbank.account_service.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSavedAccountsRequest extends BaseRequest{

    @JsonProperty(required = false)
    private String query;
}