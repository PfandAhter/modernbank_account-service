package com.modernbank.account_service.rest.controller.api.response;

import com.modernbank.account_service.model.dto.AccountDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetAccountsResponse extends BaseResponse{
    private List<AccountDTO> accounts;
    private String firstName;
    private String secondName;
    private String lastName;
}