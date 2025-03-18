package com.modernbank.account_service.rest.service.account;

import com.modernbank.account_service.api.request.CreateAccountRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.model.entity.Account;
import com.modernbank.account_service.model.entity.Branch;
import com.modernbank.account_service.model.entity.User;
import com.modernbank.account_service.model.enums.AccountStatus;
import com.modernbank.account_service.repository.AccountRepository;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;

    private final BranchRepository branchRepository;

    public BaseResponse createAccount(CreateAccountRequest request){

        Branch branch = branchRepository.findBranchById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found ")); //TODO: BURAYI CUSTOM EXCEPTION YAP...

        //TODO: BURADA TOKENDEN USER EXTRACT EDILICEK...
        User user = new User();

        accountRepository.save(Account.builder()
                .iban(Util.generateIban())
                .branch(branch)
                .name(request.getName())
                .balance(0.0)
                .user(user)
                .description(request.getDescription())
                .currency(request.getCurrency())
                .status(AccountStatus.ACTIVE)
                .createdDate(LocalDateTime.now())
                .build());

        return new BaseResponse(); //TODO: Burada constructorunun icerisine description verince calisicak sekilde duzenle...
    }
}