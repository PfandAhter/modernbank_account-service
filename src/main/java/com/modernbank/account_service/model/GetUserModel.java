package com.modernbank.account_service.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserModel {
    private String id;
    private String email;
    private String firstName;
    private String secondName;
    private String lastName;
}