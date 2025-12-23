package com.modernbank.account_service.api.response;

import com.modernbank.account_service.api.dto.UserDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class UserDetailsResponse {
    UserDetailsDTO userDetails;
}