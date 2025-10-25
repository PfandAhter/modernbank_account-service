package com.modernbank.account_service.api.response;

import com.modernbank.account_service.api.dto.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAccountDetailsResponse {
    private AccountDTO account;
}