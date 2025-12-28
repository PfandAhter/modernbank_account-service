package com.modernbank.account_service.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AdminUpdateUserRoleRequest extends BaseRequest{
    private String userIdInfo;

    private String role;
}