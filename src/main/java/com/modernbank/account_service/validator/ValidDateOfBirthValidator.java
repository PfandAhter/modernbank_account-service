package com.modernbank.account_service.validator;

import com.modernbank.account_service.validator.annotations.ValidDateOfBirth;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidDateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, String> {

    private final Pattern DATE_REGEX = Pattern.compile("^(0[1-9]|1[0-2])/([0-2][0-9]|3[01])/\\d{4}$");

    @Override
    public boolean isValid(String dateOfBirth, ConstraintValidatorContext context) {
        return Pattern.matches(DATE_REGEX.pattern(), dateOfBirth);
    }
}