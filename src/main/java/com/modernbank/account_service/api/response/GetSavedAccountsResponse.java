package com.modernbank.account_service.api.response;

import com.modernbank.account_service.api.dto.SavedAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSavedAccountsResponse {
    private List<SavedAccountDTO> savedAccounts;
}