package com.modernbank.account_service.validator;

import com.modernbank.account_service.validator.annotations.ValidString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidStringValidator implements ConstraintValidator<ValidString,String> {

    // NOTE: The regex pattern below was changed from requiring both uppercase and lowercase letters
    // to allowing any combination of letters and spaces (including Turkish characters).
    // This is a breaking change in validation behavior. The new pattern allows strings containing
    // only letters (a-z, A-Z, Turkish characters) and spaces, with a length between 1 and 255.
    // Previous pattern required both uppercase and lowercase letters; now, any mix is accepted.
    private final Pattern STRING_REGEX = Pattern.compile("^[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]{1,255}$");

    @Override
    public boolean isValid(String string, ConstraintValidatorContext context) {
        return Pattern.matches(STRING_REGEX.pattern(), string);
    }
}