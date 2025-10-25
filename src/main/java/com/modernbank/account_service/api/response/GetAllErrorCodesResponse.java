package com.modernbank.account_service.api.response;

import com.modernbank.account_service.api.dto.ErrorCodesDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class GetAllErrorCodesResponse {
    private List<ErrorCodesDTO> errorCodes;
}