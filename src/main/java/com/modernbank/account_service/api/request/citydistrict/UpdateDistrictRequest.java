package com.modernbank.account_service.api.request.citydistrict;

import com.modernbank.account_service.api.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter

public class UpdateDistrictRequest extends BaseRequest {
    private Long id;
    private String name;
    private Map<String, Long> branchIds;

    private String status;
}