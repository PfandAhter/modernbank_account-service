package com.modernbank.account_service.validator;

import com.modernbank.account_service.repository.UserRepository;
import com.modernbank.account_service.validator.annotations.UniqueTCKN;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueTCKNValidator implements ConstraintValidator<UniqueTCKN,String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String tckn, ConstraintValidatorContext context) {
        return userRepository.findByTCKN(tckn).isEmpty();
    }
}