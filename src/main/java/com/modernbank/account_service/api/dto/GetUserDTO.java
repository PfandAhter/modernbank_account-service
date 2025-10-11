package com.modernbank.account_service.api.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetUserDTO {
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