package com.modernbank.account_service.validator;

import com.modernbank.account_service.model.enums.Currency;
import com.modernbank.account_service.validator.annotations.ValidCurrency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCurrencyValidator implements ConstraintValidator<ValidCurrency, Currency> {

    @Override
    public boolean isValid(Currency currency, ConstraintValidatorContext context) {
        for (Currency c : Currency.values()) {
            if (c.equals(currency)) {
                return true;
            }
        }
        return false;
    }
}