package com.modernbank.account_service.api.response;

import com.modernbank.account_service.api.dto.GetUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserInfoResponse extends BaseResponse{
    private GetUserDTO user;
}