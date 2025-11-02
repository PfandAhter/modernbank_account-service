package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.api.request.GetAccountDetailsRequest;
import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.AccountListModel;
import com.modernbank.account_service.model.AccountModel;
import com.modernbank.account_service.repository.UserRepository;
import com.modernbank.account_service.api.request.CreateAccountRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.entity.Account;
import com.modernbank.account_service.entity.Branch;
import com.modernbank.account_service.entity.User;
import com.modernbank.account_service.model.enums.AccountStatus;
import com.modernbank.account_service.repository.AccountRepository;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.api.response.GetAccountByIBAN;
import com.modernbank.account_service.api.response.GetAccountOwnerNameResponse;
import com.modernbank.account_service.rest.service.AccountService;
import com.modernbank.account_service.rest.service.MapperService;
import com.modernbank.account_service.rest.service.cache.account.IAccountCacheService;
import com.modernbank.account_service.util.IbanGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.modernbank.account_service.constants.ErrorCodeConstants.BRANCH_NOT_FOUND;
import static com.modernbank.account_service.constants.ErrorCodeConstants.USER_NOT_FOUND;
import static com.modernbank.account_service.constants.ErrorCodeConstants.ACCOUNT_NOT_FOUND_BY_IBAN;
import static com.modernbank.account_service.constants.ErrorCodeConstants.ACCOUNT_NOT_FOUND;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final BranchRepository branchRepository;

    private final IAccountCacheService accountCacheService;

    private final UserRepository userRepository;

    private final IbanGenerationService ibanGenerationService;

    private final MapperService mapperService;

    @Override
    public BaseResponse createAccount(CreateAccountRequest request) {

        Branch branch = branchRepository.findBranchById(request.getBranchId())
                .orElseThrow(() -> new NotFoundException(BRANCH_NOT_FOUND));

        User user = userRepository.findByUserId(request.getUserId())
                        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        accountRepository.save(Account.builder()
                .iban(ibanGenerationService.generateUniqueIban())
                .branch(branch)
                .name(request.getName())
                .balance(0.0)
                .user(user)
                .description(request.getDescription())
                .currency(request.getCurrency())
                .status(AccountStatus.ACTIVE)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build());

        return new BaseResponse("Account created successfully");
    }
    /*public BaseResponse updateAccount(BaseRequest request){
        Account account = accountRepository.findAccountById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found")); //TODO: BURAYI CUSTOM EXCEPTION YAP...

        account.setName(request.getName());
        account.setDescription(request.getDescription());
        account.setCurrency(request.getCurrency());
        account.setStatus(request.getStatus());

        accountRepository.save(account);

        return new BaseResponse(); //TODO: Burada constructorunun icerisine description verince calisicak sekilde duzenle...
    }*/

    @Override
    public AccountListModel getAccountsByUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
//        accountCacheService.refreshAccountsByUserId(); //TODO: BUNLARI BIR UCA BAGLA DA DISARIDAN MUDAHELE EDILEBILIR SEKILDE OLSUN.

        return AccountListModel.builder()
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .lastName(user.getLastName())
                .accounts(accountCacheService.getAccountsByUserId(user.getId()))
                .build();
    }

    @Override
    public AccountModel getAccountDetails(GetAccountDetailsRequest request) {
        Account account = accountRepository.findAccountById(request.getAccountId())
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));

        return mapperService.map(account, AccountModel.class);
    }

    @Override
    public GetAccountByIBAN getAccountByIBAN(String iban) {
        Account account = accountRepository.findAccountByIban(iban)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND_BY_IBAN));

        User user = account.getUser();
        /*
        * 
        * */
        String tckn = user.getTckn();
        if (tckn != null) {
            int visible = Math.min(4, tckn.length());
            String visiblePart = tckn.substring(0, visible);
            String maskedRest = "*".repeat(Math.max(0, tckn.length() - visible));
            tckn = visiblePart + maskedRest;
        }

        return GetAccountByIBAN.builder()
                .accountId(account.getId())
                .accountName(account.getName())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .lastName(user.getLastName())
                .tckn(tckn)
                .email(user.getEmail())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .accountStatus(account.getStatus())
                .build();
    }

    @Override
    public GetAccountOwnerNameResponse getAccountOwnerName(String iban) {
        Account account = accountRepository.findAccountByIban(iban)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));

        User user = account.getUser();

        return GetAccountOwnerNameResponse.builder()
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .lastName(user.getLastName())
                .build();
    }

    public BaseResponse updateBalance(String iban, double balance) {
        Account account = accountRepository.findAccountByIban(iban)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));

        account.setBalance(account.getBalance() + balance);
        account.setUpdatedDate(LocalDateTime.now());

        accountRepository.save(account);

        return new BaseResponse("Balance updated successfully new amount:" + account.getBalance());
    }
}