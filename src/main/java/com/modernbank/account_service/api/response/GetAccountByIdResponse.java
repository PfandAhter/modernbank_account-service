package com.modernbank.account_service.api.response;

import com.modernbank.account_service.api.dto.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAccountByIdResponse {
    AccountDTO account;
}