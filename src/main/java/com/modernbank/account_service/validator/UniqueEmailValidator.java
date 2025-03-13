package com.modernbank.account_service.validator;

import com.modernbank.account_service.repository.UserRepository;
import com.modernbank.account_service.validator.annotations.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail,String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, jakarta.validation.ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.findByEmail(email).isEmpty() || checkEmailEndsWith(email);
    }

    private boolean checkEmailEndsWith(String email){
        return email.endsWith("@gmail.com") || email.endsWith("@outlook.com") || email.endsWith("@hotmail.com");
    }
}