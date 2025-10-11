package com.modernbank.account_service.api.request;

import com.modernbank.account_service.model.enums.Currency;
import com.modernbank.account_service.validator.annotations.ValidCurrency;
import com.modernbank.account_service.validator.annotations.ValidString;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import static com.modernbank.account_service.constants.ErrorMessageConstants.ACCOUNT_NAME_NOT_VALID;
import static com.modernbank.account_service.constants.ErrorMessageConstants.ACCOUNT_NAME_NOT_BLANK;
import static com.modernbank.account_service.constants.ErrorMessageConstants.CURRENCY_NOT_BLANK;
import static com.modernbank.account_service.constants.ErrorMessageConstants.CURRENCY_NOT_VALID;
import static com.modernbank.account_service.constants.ErrorMessageConstants.STRING_NOT_BLANK;
import static com.modernbank.account_service.constants.ErrorMessageConstants.STRING_NOT_VALID;

@Getter
@Setter
public class CreateAccountRequest extends BaseRequest {

    @ValidString(message = ACCOUNT_NAME_NOT_VALID)
    @NotBlank(message = ACCOUNT_NAME_NOT_BLANK)
    private String name;

    @ValidCurrency(message = CURRENCY_NOT_VALID)
    @NotBlank(message = CURRENCY_NOT_BLANK)
    private Currency currency;

    private long branchId;

    @ValidString(message = STRING_NOT_VALID)
    @NotBlank(message = STRING_NOT_BLANK)
    private String description;
}
