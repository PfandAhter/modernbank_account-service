package com.modernbank.account_service.api.request.citydistrict;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateCityRequest extends CreateCityRequest{
    private Long id;

    private String status;
}