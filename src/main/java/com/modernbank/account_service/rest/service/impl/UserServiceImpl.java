package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.api.request.*;
import com.modernbank.account_service.entity.SavedAccount;
import com.modernbank.account_service.entity.User;
import com.modernbank.account_service.exception.BusinessException;
import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.GetUserModel;
import com.modernbank.account_service.model.SavedAccountModel;
import com.modernbank.account_service.model.UserDetailsModel;
import com.modernbank.account_service.model.enums.Role;
import com.modernbank.account_service.repository.AccountRepository;
import com.modernbank.account_service.repository.SavedAccountRepository;
import com.modernbank.account_service.repository.UserRepository;
import com.modernbank.account_service.rest.service.MapperService;
import com.modernbank.account_service.rest.service.OTPCodeService;
import com.modernbank.account_service.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.modernbank.account_service.constants.ErrorCodeConstants.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final Pbkdf2PasswordEncoder passwordEncoder;

    private final OTPCodeService otpCodeService;

    private final MapperService mapperService;

    private final SavedAccountRepository savedAccountRepository;

    public void createUser(CreateUserRequest request) {

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new BusinessException(USER_ALREADY_EXISTS);
        });

        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BusinessException("USER_NAME_CANNOT_BE_NULL_OR_EMPTY");
        }
        String[] nameParts = request.getName().trim().split("\\s+");
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : "";
        String secondName = "";
        if (nameParts.length > 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < nameParts.length - 1; i++) {
                sb.append(nameParts[i]);
                if (i < nameParts.length - 2) {
                    sb.append(" ");
                }
            }
            secondName = sb.toString();
        }

        userRepository.save(User.builder()
                .tckn("")
                .firstName(firstName)
                .secondName(secondName)
                .lastName(lastName)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .gsm(request.getGsm())
                .dateOfBirth("")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .authorities(roles)
                .address("")
                .accounts(new ArrayList<>())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(false)
                .build());

        otpCodeService.createUserVerificationOTP(request.getEmail(), "CREATION");
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
    public void updateUserInfo(UpdateUserInfoRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + request.getEmail())); // TODO: Burada ki gibi dynamic value olanlari baska bir sekilde handle edeyim.

        if (user.getTckn() != null && request.getTckn() != null) {
            throw new BusinessException(TCKN_CANNOT_BE_CHANGED);
        } else {
            if (request.getTckn() != null) {
                user.setTckn(request.getTckn());
            }
        }

        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }

        if (request.getEmail() != null && user.getEmail() != null) {
            throw new BusinessException(EMAIL_CANNOT_BE_CHANGED);
        } else {
            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }
        }

        if (request.getGsm() != null) {
            user.setGsm(request.getGsm());
        }

        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }

        user.setUpdatedDate(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void addSavedAccount(AddSavedAccountRequest request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with userId: " + request.getUserId())); // TODO: Burada ki gibi dynamic value olanlari baska bir sekilde handle edeyim.

        user.getSavedAccounts().forEach(account -> {
            if (account.getAccountIBAN().equals(request.getAccountIBAN())) {
                throw new BusinessException("SAVED_ACCOUNT_ALREADY_EXISTS");// TODO: Burada ki gibi dynamic value olanlari baska bir sekilde handle edeyim.
            }
            if (account.getNickname().equals(request.getNickname())) {
                throw new BusinessException("SAVED_ACCOUNT_NICKNAME_ALREADY_EXISTS");
            }
        });

        SavedAccount savedAccount = SavedAccount.builder()
                .accountIBAN(request.getAccountIBAN())
                .nickname(request.getNickname())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .user(user)
                .secondName(request.getSecondName())
                .build();
        savedAccountRepository.save(savedAccount);
        user.getSavedAccounts().add(savedAccount);
        user.setUpdatedDate(LocalDateTime.now());
        userRepository.save(user);//TODO: BURADA IBANIN DOGRULU KONTROLU DAHA SONRA YAPILACAKTIR...
    }

    public void removeSavedAccount(RemoveSavedAccountRequest request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with userId: " + request.getUserId())); // TODO: Burada ki gibi dynamic value olanlari baska bir sekilde handle edeyim.

        SavedAccount savedAccount = savedAccountRepository.findByIdAndUser(request.getId(), user.getId())
                .orElseThrow(() -> new NotFoundException("Saved account not found with id: " + request.getId()));

        user.getSavedAccounts().remove(savedAccount);
        savedAccountRepository.delete(savedAccount);
        user.setUpdatedDate(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void updateUserRole(AdminUpdateUserRoleRequest request) {
        log.info("Updating user role: userId={}, newRole={}", request.getUserIdInfo(), request.getRole());
        User user = userRepository.findByUserId(request.getUserIdInfo())
                .orElseThrow(() -> new NotFoundException("User not found with userId: " + request.getUserIdInfo())); // TODO: Burada ki gibi dynamic value olanlari baska bir sekilde handle edeyim.

        Set<Role> newRoles = new HashSet<>();
        newRoles.add(Role.valueOf(request.getRole()));

        user.getAuthorities().clear();
        user.setAuthorities(newRoles);
        user.setUpdatedDate(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(AdminDeleteUserRequest request) {
        log.info("Deleting user: userId={}", request.getUserId());

        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with userId: " + request.getUserId())); // TODO: Burada ki gibi dynamic value olanlari baska bir sekilde handle edeyim.

        accountRepository.deleteAccountsByUserId(user.getId());
        userRepository.delete(user);
    }

    @Override
    public List<SavedAccountModel> getSavedAccounts(GetSavedAccountsRequest request) {
        String userId = request.getUserId();
        String query = request.getQuery();

        List<SavedAccount> savedAccounts;

        if (query == null || query.trim().isEmpty()) {
            // tüm kayıtlı alıcılar
            savedAccounts = savedAccountRepository.findAllByUserId(userId)
                    .orElseThrow(() -> new NotFoundException("No saved accounts found for userId: " + userId));
        } else {
            // filtreli arama (isim veya nickname)
            savedAccounts = savedAccountRepository.findByUserIdAndNameOrNicknameLike(userId, query)
                    .orElseThrow(() -> new NotFoundException("No saved accounts found for query: " + query));
        }

        return mapperService.map(savedAccounts, SavedAccountModel.class);
    }

    @Override
    public UserDetailsModel getUserDetailsByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        return UserDetailsModel.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .roles(user.getAuthorities())
                .build();
    }
}