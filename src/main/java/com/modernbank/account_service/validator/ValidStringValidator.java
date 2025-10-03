package com.modernbank.account_service.validator;

import com.modernbank.account_service.validator.annotations.ValidString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidStringValidator implements ConstraintValidator<ValidString,String> {

    private final Pattern STRING_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z]).{0,255}$");

    @Override
    public boolean isValid(String string, ConstraintValidatorContext context) {
        return Pattern.matches(STRING_REGEX.pattern(), string);
    }
}