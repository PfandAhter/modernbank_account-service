package com.modernbank.account_service.rest.controller.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({
        "Status",
        "ProcessCode",
        "ProcessMessage"
})
public class BaseResponse {

    @JsonProperty("Status")
    private String status = ResponseStatus.SUCCESS_CODE;

    @JsonProperty("ProcessCode")
    private String processCode = ErrorCodeConstants.SUCCESS;

    @JsonProperty("ProcessMessage")
    private String processMessage = ResponseStatus.PROCESS_SUCCESS;

    public BaseResponse(String processMessage){
        this.processCode = ResponseStatus.PROCESS_SUCCESS;
        this.processMessage = processMessage;
    }
}