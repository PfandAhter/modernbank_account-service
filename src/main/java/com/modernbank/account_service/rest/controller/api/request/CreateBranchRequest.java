package com.modernbank.account_service.rest.controller.api.request;

import com.modernbank.account_service.validator.annotations.ValidString;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import static com.modernbank.account_service.constants.ErrorMessageConstants.BRANCH_NAME_NOT_BLANK;
import static com.modernbank.account_service.constants.ErrorMessageConstants.BRANCH_NAME_NOT_VALID;

import static com.modernbank.account_service.constants.ErrorMessageConstants.ADDRESS_NOT_BLANK;
import static com.modernbank.account_service.constants.ErrorMessageConstants.ADDRESS_NOT_VALID;

import static com.modernbank.account_service.constants.ErrorMessageConstants.CITY_NOT_BLANK;
import static com.modernbank.account_service.constants.ErrorMessageConstants.CITY_NOT_VALID;

import static com.modernbank.account_service.constants.ErrorMessageConstants.COUNTRY_NOT_BLANK;
import static com.modernbank.account_service.constants.ErrorMessageConstants.COUNTRY_NOT_VALID;


@Getter
@Setter

public class CreateBranchRequest extends BaseRequest{

    @ValidString(message = BRANCH_NAME_NOT_VALID)
    @NotBlank(message = BRANCH_NAME_NOT_BLANK)
    private String name;

    @ValidString(message = ADDRESS_NOT_VALID)
    @NotBlank(message = ADDRESS_NOT_BLANK)
    private String address;

    @ValidString(message = CITY_NOT_VALID)
    @NotBlank(message = CITY_NOT_BLANK)
    private String city;

    @ValidString(message = COUNTRY_NOT_VALID)
    @NotBlank(message = COUNTRY_NOT_BLANK)
    private String country;
}