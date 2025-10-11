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
    private String tckn;
    private String phoneNumber;
    private String address;
    private String birthDate;
    private String firstName;
    private String secondName;
    private String lastName;
}