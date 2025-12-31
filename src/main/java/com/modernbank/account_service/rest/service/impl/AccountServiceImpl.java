package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.api.request.GetAccountDetailsRequest;
import com.modernbank.account_service.api.request.account.AddIBANBlackListRequest;
import com.modernbank.account_service.api.request.account.RemoveIBANBlackListRequest;
import com.modernbank.account_service.api.response.AccountProfileResponse;
import com.modernbank.account_service.entity.*;
import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.AccountListModel;
import com.modernbank.account_service.model.AccountModel;
import com.modernbank.account_service.model.CreateCardModel;
import com.modernbank.account_service.model.enums.CardNetwork;
import com.modernbank.account_service.model.enums.CardType;
import com.modernbank.account_service.repository.BlacklistRepository;
import com.modernbank.account_service.repository.CardRepository;
import com.modernbank.account_service.repository.UserRepository;
import com.modernbank.account_service.api.request.CreateAccountRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.model.enums.AccountStatus;
import com.modernbank.account_service.repository.AccountRepository;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.api.response.GetAccountByIBAN;
import com.modernbank.account_service.api.response.GetAccountOwnerNameResponse;
import com.modernbank.account_service.rest.service.AccountService;
import com.modernbank.account_service.rest.service.CardService;
import com.modernbank.account_service.rest.service.MapperService;
import com.modernbank.account_service.rest.service.cache.account.IAccountCacheService;
import com.modernbank.account_service.util.IbanGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.modernbank.account_service.constants.ErrorCodeConstants.BRANCH_NOT_FOUND;
import static com.modernbank.account_service.constants.ErrorCodeConstants.USER_NOT_FOUND;
import static com.modernbank.account_service.constants.ErrorCodeConstants.ACCOUNT_NOT_FOUND_BY_IBAN;
import static com.modernbank.account_service.constants.ErrorCodeConstants.ACCOUNT_NOT_FOUND;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

    private final CardService cardService;

    private final CardRepository cardRepository;

    private final BlacklistRepository blacklistRepository;

    @Value("${account.daily-limit.transfer:50000.0}")
    private Double dailyTransferLimit;

    @Value("${account.daily-limit.withdraw:10000.0}")
    private Double dailyWithdrawLimit;

    @Value("${account.daily-limit.deposit:10000.0}")
    private Double dailyDepositLimit;

    @Override
    public BaseResponse createAccount(CreateAccountRequest request) {
        log.info("Creating account for userId: {}", request.getUserId());
        Branch branch = branchRepository.findBranchById(request.getBranchId())
                .orElseThrow(() -> new NotFoundException(BRANCH_NOT_FOUND));

        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        Account account = Account.builder()
                .iban(ibanGenerationService.generateUniqueIban())
                .branch(branch)
                .name(request.getName())
                .balance(0.0)
                .user(user)
                .description(request.getDescription())
                .dailyTransferLimit(dailyTransferLimit)
                .dailyWithdrawLimit(dailyWithdrawLimit)
                .dailyDepositLimit(dailyDepositLimit)
                .previousFraudCount(0)
                .previousFraudFlag(false)
                .currency(request.getCurrency())
                .status(AccountStatus.ACTIVE)
                .cards(new ArrayList<>())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        accountRepository.save(account);

        cardService.createCardForAccount(CreateCardModel.builder()
                .cardNetwork(CardNetwork.VISA.getBinPrefix())
                .cardType(CardType.DEBIT.toString())
                .account(account)
                .build());
        return new BaseResponse("Account created successfully");
    }

    @Override
    public AccountListModel getAccountsByUser(String userId) {
        log.info("Fetching accounts for userId: {}", userId);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return AccountListModel.builder()
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .lastName(user.getLastName())
                .accounts(accountCacheService.getAccountsByUserId(user.getId()))
                .build();
    }

    @Override
    public AccountModel getAccountDetails(GetAccountDetailsRequest request) {
        log.info("Getting account details for accountId: {}", request.getAccountId());
        Account account = getAccountEntityById(request.getAccountId());

        return mapperService.map(account, AccountModel.class);
    }

    @Override
    public GetAccountByIBAN getAccountByIBAN(String iban) {
        log.info("Getting account details for IBAN: {}", iban);
        Account account = getAccountEntityByIBAN(iban);

        User user = account.getUser();

        String tckn = user.getTckn();
        if (tckn != null) {
            int length = tckn.length();
            if (length > 4) {
                int visibleStart = 2;
                int visibleEnd = 2;
                String start = tckn.substring(0, visibleStart);
                String end = tckn.substring(length - visibleEnd);
                StringBuilder maskedMiddle = new StringBuilder();
                for (int i = 0; i < length - visibleStart - visibleEnd; i++) {
                    maskedMiddle.append('*');
                }
                tckn = start + maskedMiddle.toString() + end;
            }
        }

        return GetAccountByIBAN.builder()
                .accountId(account.getId())
                .accountName(account.getName())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .lastName(user.getLastName())
                .tckn(tckn)
                .dailyTransferLimit(account.getDailyTransferLimit())
                .dailyDepositLimit(account.getDailyDepositLimit())
                .dailyWithdrawLimit(account.getDailyWithdrawLimit())
                .email(user.getEmail())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .accountStatus(account.getStatus())
                .build();
    }

    @Override
    public AccountModel getAccountById(String accountId) {
        Account account = getAccountEntityById(accountId);

        AccountModel accountModel = mapperService.map(account, AccountModel.class);
        User user = account.getUser();
        accountModel.setFirstName(user.getFirstName());
        accountModel.setSecondName(user.getSecondName());
        accountModel.setLastName(user.getLastName());
        accountModel.setUserId(user.getId());
        return accountModel;
    }

    @Override
    public GetAccountOwnerNameResponse getAccountOwnerName(String iban) {
        log.info("Getting account owner name for IBAN: {}", iban);
        Account account = getAccountEntityByIBAN(iban);

        User user = account.getUser();

        return GetAccountOwnerNameResponse.builder()
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public BaseResponse updateBalance(String iban, double balance) {
        log.info("Updating balance for IBAN: {} with amount: {}", iban, balance);
        Account account = getAccountEntityByIBAN(iban);

        account.setBalance(account.getBalance() + balance);
        account.setUpdatedDate(LocalDateTime.now());

        accountRepository.save(account);

        try {
            accountCacheService.updateAccount(account);
        } catch (Exception e) {
            log.warn("Failed to update account cache for iban {}: {}", iban, e.getMessage());
        }

        return new BaseResponse("Balance updated successfully new amount:" + account.getBalance());
    }

    @Override
    public void updateAccountLimit(String accountId, double amount, String category) {
        log.info("Updating account limit for accountId: {} with amount: {} for category: {}", accountId, amount, category);
        Account account = getAccountEntityById(accountId);

        switch (category.toUpperCase()) {
            case "TRANSFER":
                account.setDailyTransferLimit(account.getDailyTransferLimit() - amount);
                break;
            case "WITHDRAWAL":
                account.setDailyWithdrawLimit(account.getDailyWithdrawLimit() - amount);
                break;
            case "DEPOSIT":
                account.setDailyDepositLimit(account.getDailyDepositLimit() - amount);
                break;
            default:
                log.warn("Invalid category provided for account limit update: {}", category);
                throw new IllegalArgumentException("Invalid category: " + category);
        }

        account.setUpdatedDate(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Override
    public AccountProfileResponse getAccountProfile(String accountId) {
        log.info("Getting account profile for accountId: {}", accountId);
        Account account = getAccountEntityById(accountId);

        User user = account.getUser();

        Integer accountAgeMonths = null;
        if (account.getCreatedDate() != null) {
            accountAgeMonths = (int) ChronoUnit.MONTHS.between(account.getCreatedDate(), LocalDateTime.now());
        }

        List<Card> cards = cardRepository.findByAccountId(account.getId());

        if (cards.isEmpty()) {
            return AccountProfileResponse.builder()
                    .accountId(account.getId())
                    .userId(user.getId())
                    .iban(account.getIban())
                    .accountName(account.getName())
                    .accountAgeMonths(accountAgeMonths)
                    .creditScore(account.getCreditScore())

                    .currentBalance(account.getBalance())
                    .previousFraudFlag(account.getPreviousFraudFlag() != null ? account.getPreviousFraudFlag() : false)
                    .cardAgeMonths(0)
                    .cardType("DEBIT")

                    .previousFraudCount(account.getPreviousFraudCount() != null ? account.getPreviousFraudCount() : 0)
                    .accountStatus(account.getStatus() != null ? account.getStatus().name() : null)
                    .build();
        }

        Card firstCreatedCard = cards.stream()
                .filter(c -> c != null && c.getCreatedDate() != null)
                .min((c1, c2) -> c1.getCreatedDate().compareTo(c2.getCreatedDate()))
                .orElse(null);

        Integer cardAgeMonths = null;
        if (firstCreatedCard != null && firstCreatedCard.getCreatedDate() != null) {
            cardAgeMonths = (int) ChronoUnit.MONTHS.between(firstCreatedCard.getCreatedDate(), LocalDateTime.now());
        }

        return AccountProfileResponse.builder()
                .accountId(account.getId())
                .userId(user.getId())
                .iban(account.getIban())
                .accountName(account.getName())
                .accountAgeMonths(accountAgeMonths)
                .creditScore(account.getCreditScore())

                .currentBalance(account.getBalance())
                .previousFraudFlag(account.getPreviousFraudFlag())
                .cardAgeMonths(cardAgeMonths)
                .cardType(firstCreatedCard.getType().toString())

                .previousFraudCount(account.getPreviousFraudCount() != null ? account.getPreviousFraudCount() : 0)
                .accountStatus(account.getStatus() != null ? account.getStatus().name() : null)
                .build();
    }

    @Override
    public BaseResponse holdAccount(String accountId) {
        log.info("Holding account with ID: {}", accountId);
        Account account = getAccountEntityById(accountId);

        account.setStatus(AccountStatus.HOLD);
        account.setUpdatedDate(LocalDateTime.now());
        accountRepository.save(account);

        try {
            accountCacheService.updateAccount(account);
        } catch (Exception e) {
            log.warn("Failed to update cache for held account {}: {}", accountId, e.getMessage());
        }

        log.info("Account {} has been put on HOLD status", accountId);
        return new BaseResponse("Account has been put on hold due to suspicious activity");
    }

    @Override
    public Boolean isReceiverBlacklisted(String iban) {
        log.info("Checking if IBAN is blacklisted: {}", iban);
        boolean isBlacklisted = blacklistRepository.existsByIbanAndIsActiveTrue(iban);
        log.info("IBAN {} blacklist status: {}", iban, isBlacklisted);
        return isBlacklisted;
    }

    private static final int FRAUD_COUNT_THRESHOLD = 3;
    private static final int BLOCK_DURATION_HOURS = 24;

    @Override
    public Boolean validateAccountNotBlocked(String accountId) {
        log.info("Validating account not blocked for accountId: {}", accountId);
        Account account = getAccountEntityById(accountId);

        LocalDateTime blockedUntil = account.getBlockedUntil();

        if (blockedUntil != null && blockedUntil.isAfter(LocalDateTime.now())) {
            log.warn("Account {} is blocked until {}", accountId, blockedUntil);
            return true;
//            throw new AccountBlockedException(iban, blockedUntil);
        }

        log.info("Account {} is not blocked, operation allowed", accountId);

        return false;
    }

    @Override
    public BaseResponse incrementFraudCount(String accountId, String reason) {
        log.info("Incrementing fraud count for AccountId: {} , for this reason: {}", accountId, reason);
        Account account = getAccountEntityById(accountId);

        int currentCount = account.getPreviousFraudCount() != null ? account.getPreviousFraudCount() : 0;
        int newCount = currentCount + 1;

        log.info("Account {} fraud count: {} -> {}", accountId, currentCount, newCount);

        account.setPreviousFraudFlag(true);

        if (newCount >= FRAUD_COUNT_THRESHOLD) {
            LocalDateTime blockUntil = LocalDateTime.now().plusHours(BLOCK_DURATION_HOURS);
            account.setBlockedUntil(blockUntil);
            account.setPreviousFraudCount(0);
            account.setUpdatedDate(LocalDateTime.now());
            account.setStatus(AccountStatus.BLOCKED);
            account.setBlockedReason(reason);
            accountRepository.save(account);

            log.warn("Account {} has reached fraud threshold. Soft block applied until {}", account.getId(), blockUntil);

            try {
                accountCacheService.updateAccount(account);
            } catch (Exception e) {
                log.warn("Failed to update cache for blocked account {}: {}", account.getId(), e.getMessage());
            }

            return new BaseResponse(
                    "Account temporarily blocked for " + BLOCK_DURATION_HOURS + " hours due to suspicious activity");
        }

        account.setPreviousFraudCount(newCount);
        account.setUpdatedDate(LocalDateTime.now());
        accountRepository.save(account);

        try {
            accountCacheService.updateAccount(account);
        } catch (Exception e) {
            log.warn("Failed to update cache for account {}: {}", account.getId(), e.getMessage());
        }

        return new BaseResponse("Fraud count incremented to " + newCount);
    }

    @Override
    public void updatePreviousFraudFlag(String accountId, boolean flag) {
        log.info("Updating previous fraud flag for AccountId: {} to {}", accountId, flag);
        Account account = getAccountEntityById(accountId);

        account.setPreviousFraudFlag(flag);
        account.setUpdatedDate(LocalDateTime.now());
        accountRepository.save(account);

        try {
            accountCacheService.updateAccount(account);
        } catch (Exception e) {
            log.warn("Failed to update cache for account {}: {}", account.getId(), e.getMessage());
        }
    }

    @Override
    public void addIBANBlackList(AddIBANBlackListRequest request) {
        log.info("Adding IBAN to blacklist: {} by: {}", request.getIban(), request.getUserId());
        blacklistRepository.save(Blacklist.builder()
                .iban(request.getIban())
                .reason(request.getReason())
                .blacklistedBy(request.getUserId())
                .removedDate(null)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build());
    }

    @Override
    public void removeIBANBlackList(RemoveIBANBlackListRequest request) {
        log.info("Removing IBAN from blacklist: {} by: {}", request.getIban(), request.getUserId());
        Blacklist blacklist = blacklistRepository.findByIbanAndIsActiveTrue(request.getIban())
                .orElseThrow(() -> new NotFoundException("Blacklist entry not found for IBAN: " + request.getIban())); //TODO: Generic exception with args.

        blacklist.setIsActive(false);
        blacklist.setRemovedBy(request.getUserId());
        blacklist.setRemovedDate(LocalDateTime.now());

        blacklistRepository.save(blacklist);
    }


    private Account getAccountEntityByIBAN(String iban){
        return accountRepository.findAccountByIban(iban)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND_BY_IBAN));
    }

    private Account getAccountEntityById(String accountId){
        return accountRepository.findAccountById(accountId)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Istanbul")
    public void resetDailyLimitsJob() {
        log.info("Günlük limit sıfırlama işlemi başladı...");

        double defaultTransferLimit = dailyTransferLimit;
        double defaultWithdrawLimit = dailyWithdrawLimit;
        double defaultDepositLimit = dailyDepositLimit;

        accountRepository.resetDailyLimits(defaultTransferLimit,
                defaultWithdrawLimit,
                defaultDepositLimit,
                AccountStatus.ACTIVE
        );

        try {
            accountCacheService.refreshAccountsByUserId();
        } catch (Exception e) {
            log.warn("Limit reset sonrası cache temizlenirken hata oluştu: {}", e.getMessage());
        }

        log.info("Tüm aktif hesapların günlük limitleri başarıyla sıfırlandı.");
    }
}