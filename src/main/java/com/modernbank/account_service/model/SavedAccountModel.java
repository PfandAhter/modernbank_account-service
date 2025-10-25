package com.modernbank.account_service.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavedAccountModel {
    private String id;

    private String nickname;

    private String accountIBAN;

    private String firstName;

    private String secondName;

    private String lastName;
}