package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.api.request.UpdateUserInfoRequest;
import com.modernbank.account_service.entity.User;
import com.modernbank.account_service.exception.BusinessException;
import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.GetUserModel;
import com.modernbank.account_service.model.enums.Role;
import com.modernbank.account_service.repository.UserRepository;
import com.modernbank.account_service.api.request.CreateUserRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import static com.modernbank.account_service.constants.ErrorCodeConstants.EMAIL_CANNOT_BE_CHANGED;
import static com.modernbank.account_service.constants.ErrorCodeConstants.TCKN_CANNOT_BE_CHANGED;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Pbkdf2PasswordEncoder passwordEncoder;

    public BaseResponse createUser(CreateUserRequest request) {
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
                .address("")
                .accounts(new ArrayList<>())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build());

        return new BaseResponse("Create User Success");
    }


    public GetUserModel getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));// TODO: Burada ki gibi dynamic value olanlari baska bir sekilde handle edeyim.

        return GetUserModel.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .tckn(user.getTckn())
                .phoneNumber(user.getGsm())
                .address(user.getAddress())
                .birthDate(user.getDateOfBirth())
                .secondName(user.getSecondName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public void updateUserInfo(UpdateUserInfoRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + request.getEmail())); // TODO: Burada ki gibi dynamic value olanlari baska bir sekilde handle edeyim.

        if(user.getTckn() != null && request.getTckn() != null){
            throw new BusinessException(TCKN_CANNOT_BE_CHANGED);
        }else{
            if(request.getTckn() != null){
                user.setTckn(request.getTckn());
            }
        }

        if(request.getAddress() != null){
            user.setAddress(request.getAddress());
        }

        if(request.getEmail() != null && user.getEmail() != null){
            throw new BusinessException(EMAIL_CANNOT_BE_CHANGED);
        }else{
            if(request.getEmail() != null){
                user.setEmail(request.getEmail());
            }
        }

        if(request.getGsm() != null){
            user.setGsm(request.getGsm());
        }

        if(request.getDateOfBirth() != null){
            user.setDateOfBirth(request.getDateOfBirth());
        }

        user.setUpdatedDate(LocalDateTime.now());
        userRepository.save(user);
    }
}