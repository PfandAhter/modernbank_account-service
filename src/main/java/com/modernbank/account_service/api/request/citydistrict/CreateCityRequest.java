package com.modernbank.account_service.api.request.citydistrict;

import com.modernbank.account_service.api.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CreateCityRequest extends BaseRequest {
    private String name;
}