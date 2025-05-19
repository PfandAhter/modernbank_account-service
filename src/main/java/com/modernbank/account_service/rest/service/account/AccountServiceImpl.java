package com.modernbank.account_service.rest.service.account;

import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.repository.UserRepository;
import com.modernbank.account_service.rest.controller.api.request.BaseRequest;
import com.modernbank.account_service.rest.controller.api.request.CreateAccountRequest;
import com.modernbank.account_service.rest.controller.api.response.BaseResponse;
import com.modernbank.account_service.model.entity.Account;
import com.modernbank.account_service.model.entity.Branch;
import com.modernbank.account_service.model.entity.User;
import com.modernbank.account_service.model.enums.AccountStatus;
import com.modernbank.account_service.repository.AccountRepository;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.rest.controller.api.response.GetAccountByIBAN;
import com.modernbank.account_service.rest.controller.api.response.GetAccountOwnerNameResponse;
import com.modernbank.account_service.rest.controller.api.response.GetAccountsResponse;
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
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;

    private final BranchRepository branchRepository;

    private final IAccountCacheService accountCacheService;

    private final UserRepository userRepository;

    private final IbanGenerationService ibanGenerationService;

    @Override
    public BaseResponse createAccount(CreateAccountRequest request){

        Branch branch = branchRepository.findBranchById(request.getBranchId())
                .orElseThrow(() -> new NotFoundException(BRANCH_NOT_FOUND));

        //TODO: BURADA TOKENDEN USER EXTRACT EDILICEK...
        User user = userRepository.findByTCKN("14219585746")
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
    public GetAccountsResponse getAccountsByUser(BaseRequest request){
        User user = userRepository.findByUserId("6bb91e57-032c-40db-b6ce-4e3ef459c3a0") //TODO: Burasi tokenden extract edilicek...
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return GetAccountsResponse.builder()
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .lastName(user.getLastName())
                .accounts(accountCacheService.getAccountsByUserId("6bb91e57-032c-40db-b6ce-4e3ef459c3a0"))
                .build();
    }//TODO: Buraya simdilik kendi useridmi yazdim bunu dinamiklestirirsin. Birde calisiyor mu test et...

    @Override
    public GetAccountByIBAN getAccountByIBAN (String iban) {
        Account account = accountRepository.findAccountByIban(iban)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND_BY_IBAN));

        User user = userRepository.findByTCKN(account.getUser().getTckn())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return GetAccountByIBAN.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .accountStatus(account.getStatus())
                .build();
    }

    @Override
    public GetAccountOwnerNameResponse getAccountOwnerName (String iban) {
        Account account = accountRepository.findAccountByIban(iban)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));

        User user = userRepository.findByTCKN(account.getUser().getTckn())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

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