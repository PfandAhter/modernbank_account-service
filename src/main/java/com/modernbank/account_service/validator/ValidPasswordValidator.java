package com.modernbank.account_service.validator;

import com.modernbank.account_service.validator.annotations.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String>{

    private final Pattern REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#+_\\$%\\^&\\*]).{8,}$");

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return Pattern.matches(REGEX.pattern(),password);
    }
}