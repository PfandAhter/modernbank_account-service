package com.modernbank.account_service.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBranchRequest extends BaseRequest {
    private Long id;
    private String name;
    private String address;
    private Long districtId;
    private String status;
}