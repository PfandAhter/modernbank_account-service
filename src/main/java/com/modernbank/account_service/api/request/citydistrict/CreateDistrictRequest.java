package com.modernbank.account_service.api.request.citydistrict;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CreateDistrictRequest extends CreateCityRequest{
    private Long cityId;
}