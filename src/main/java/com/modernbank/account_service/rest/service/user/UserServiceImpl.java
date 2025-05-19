package com.modernbank.account_service.rest.service.user;

import com.modernbank.account_service.model.entity.User;
import com.modernbank.account_service.model.enums.Role;
import com.modernbank.account_service.repository.UserRepository;
import com.modernbank.account_service.rest.controller.api.request.CreateUserRequest;
import com.modernbank.account_service.rest.controller.api.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService{

    private final UserRepository userRepository;

    private final Pbkdf2PasswordEncoder passwordEncoder;

    public BaseResponse createUser(CreateUserRequest request){
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.USER);

         userRepository.save(User.builder()
                .tckn(request.getTckn())
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .gsm(request.getGsm())
                .dateOfBirth(request.getDateOfBirth())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .authorities(roles)
                .accounts(new ArrayList<>())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build());

         return new BaseResponse("Create User Success");
    }



}