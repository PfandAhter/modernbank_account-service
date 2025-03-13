package com.modernbank.account_service.validator;

import com.modernbank.account_service.validator.annotations.ValidTCKN;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ValidTCKNValidator implements ConstraintValidator<ValidTCKN,String> {

    private final Pattern REGEX = Pattern.compile("^[0-9]{11,11}$");

    @Override
    public boolean isValid(String tckn, ConstraintValidatorContext context) {
        return Pattern.matches(REGEX.pattern(), tckn);
    }
}