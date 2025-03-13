package com.modernbank.account_service.validator;

import com.modernbank.account_service.repository.UserRepository;
import com.modernbank.account_service.validator.annotations.UniqueGSM;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UniqueGSMValidator implements ConstraintValidator<UniqueGSM,String> {

    private final UserRepository userRepository;

    private final Pattern REGEX = Pattern.compile("^[0-9]{10,10}$");

    @Override
    public boolean isValid(String gsm, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.findByGSM(gsm).isEmpty() || Pattern.matches(REGEX.pattern(),gsm);
    }
}