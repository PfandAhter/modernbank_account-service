package com.modernbank.account_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BranchModel {
    private long id;

    private String name;

    private String address;

    private String city;

    private String country;

    private int active;
}