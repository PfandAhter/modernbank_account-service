package com.modernbank.account_service.api.response;

import com.modernbank.account_service.constants.ErrorCodeConstants;
import com.modernbank.account_service.constants.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BaseResponse {

    private String status = ResponseStatus.SUCCESS_CODE;

    private String processCode = ErrorCodeConstants.SUCCESS;

    private String processMessage = ResponseStatus.PROCESS_SUCCESS;

    public BaseResponse(String processMessage){
        this.processCode = ResponseStatus.PROCESS_SUCCESS;
        this.processMessage = processMessage;
    }
}