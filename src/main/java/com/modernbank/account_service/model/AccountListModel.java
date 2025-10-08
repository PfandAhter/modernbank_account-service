package com.modernbank.account_service.model;

import com.modernbank.account_service.api.dto.AccountDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountListModel {
    private List<AccountDTO> accounts;
    private String firstName;
    private String secondName;
    private String lastName;
}