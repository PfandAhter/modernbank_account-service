package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.api.dto.AccountDTO;
import com.modernbank.account_service.api.request.CreateAccountRequest;
import com.modernbank.account_service.api.request.GetAccountDetailsRequest;
import com.modernbank.account_service.api.response.AccountProfileResponse;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetAccountByIBAN;
import com.modernbank.account_service.api.response.GetAccountOwnerNameResponse;
import com.modernbank.account_service.entity.Account;
import com.modernbank.account_service.entity.Branch;
import com.modernbank.account_service.entity.Card;
import com.modernbank.account_service.entity.User;
import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.AccountListModel;
import com.modernbank.account_service.model.AccountModel;
import com.modernbank.account_service.model.CreateCardModel;
import com.modernbank.account_service.model.enums.AccountStatus;
import com.modernbank.account_service.model.enums.CardType;
import com.modernbank.account_service.model.enums.Currency;
import com.modernbank.account_service.repository.AccountRepository;
import com.modernbank.account_service.repository.BlacklistRepository;
import com.modernbank.account_service.repository.BranchRepository;
import com.modernbank.account_service.repository.CardRepository;
import com.modernbank.account_service.repository.UserRepository;
import com.modernbank.account_service.rest.service.CardService;
import com.modernbank.account_service.rest.service.MapperService;
import com.modernbank.account_service.rest.service.cache.account.IAccountCacheService;
import com.modernbank.account_service.util.IbanGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private IAccountCacheService accountCacheService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IbanGenerationService ibanGenerationService;

    @Mock
    private MapperService mapperService;

    @Mock
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private BlacklistRepository blacklistRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User testUser;
    private Account testAccount;
    private Branch testBranch;
    private Card testCard;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id("user-123")
                .tckn("12345678901")
                .firstName("John")
                .secondName("Middle")
                .lastName("Doe")
                .email("john.doe@example.com")
                .gsm("5551234567")
                .build();

        testBranch = Branch.builder()
                .id(1L)
                .name("Main Branch")
                .address("123 Main St")
                .build();

        testAccount = Account.builder()
                .id("account-123")
                .iban("TR123456789012345678901234")
                .name("Test Account")
                .balance(1000.0)
                .currency(Currency.TRY)
                .user(testUser)
                .branch(testBranch)
                .status(AccountStatus.ACTIVE)
                .createdDate(LocalDateTime.now().minusMonths(6))
                .updatedDate(LocalDateTime.now())
                .creditScore(750)
                .previousFraudFlag(false)
                .previousFraudCount(0)
                .cards(new ArrayList<>())
                .build();

        testCard = Card.builder()
                .id("card-123")
                .account(testAccount)
                .type(CardType.DEBIT)
                .createdDate(LocalDateTime.now().minusMonths(3))
                .build();
    }

    // ========== CREATE ACCOUNT TESTS ==========

    @Test
    void accountService_should_return_success_when_createAccount_with_valid_request() {
        // Given
        CreateAccountRequest request = new CreateAccountRequest();
        request.setUserId("user-123");
        request.setBranchId(1L);
        request.setName("New Account");
        request.setCurrency(Currency.TRY);
        request.setDescription("Test description");

        when(branchRepository.findBranchById(1L)).thenReturn(Optional.of(testBranch));
        when(userRepository.findByUserId("user-123")).thenReturn(Optional.of(testUser));
        when(ibanGenerationService.generateUniqueIban()).thenReturn("TR123456789012345678901234");
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        BaseResponse response = accountService.createAccount(request);

        // Then
        assertNotNull(response);
        assertEquals("Account created successfully", response.getProcessMessage());
        verify(accountRepository).save(any(Account.class));
        verify(cardService).createCardForAccount(any(CreateCardModel.class));
    }

    @Test
    void accountService_should_throw_NotFoundException_when_createAccount_with_invalid_branchId() {
        // Given
        CreateAccountRequest request = new CreateAccountRequest();
        request.setUserId("user-123");
        request.setBranchId(999L);

        when(branchRepository.findBranchById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> accountService.createAccount(request));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void accountService_should_throw_NotFoundException_when_createAccount_with_invalid_userId() {
        // Given
        CreateAccountRequest request = new CreateAccountRequest();
        request.setUserId("invalid-user");
        request.setBranchId(1L);

        when(branchRepository.findBranchById(1L)).thenReturn(Optional.of(testBranch));
        when(userRepository.findByUserId("invalid-user")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> accountService.createAccount(request));
        verify(accountRepository, never()).save(any(Account.class));
    }

    // ========== GET ACCOUNTS BY USER TESTS ==========

    @Test
    void accountService_should_return_accountListModel_when_getAccountsByUser_with_valid_userId() {
        // Given
        String userId = "user-123";
        List<AccountDTO> accountDTOs = Collections.singletonList(new AccountDTO());

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(testUser));
        when(accountCacheService.getAccountsByUserId(testUser.getId())).thenReturn(accountDTOs);

        // When
        AccountListModel result = accountService.getAccountsByUser(userId);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Middle", result.getSecondName());
        assertEquals("Doe", result.getLastName());
        assertEquals(accountDTOs, result.getAccounts());
    }

    @Test
    void accountService_should_throw_NotFoundException_when_getAccountsByUser_with_invalid_userId() {
        // Given
        String userId = "invalid-user";
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> accountService.getAccountsByUser(userId));
    }

    // ========== GET ACCOUNT DETAILS TESTS ==========

    @Test
    void accountService_should_return_accountModel_when_getAccountDetails_with_valid_accountId() {
        // Given
        GetAccountDetailsRequest request = new GetAccountDetailsRequest();
        request.setAccountId("account-123");

        AccountModel accountModel = new AccountModel();
        accountModel.setId("account-123");
        accountModel.setIban("TR123456789012345678901234");

        when(accountRepository.findAccountById("account-123")).thenReturn(Optional.of(testAccount));
        when(mapperService.map(testAccount, AccountModel.class)).thenReturn(accountModel);

        // When
        AccountModel result = accountService.getAccountDetails(request);

        // Then
        assertNotNull(result);
        assertEquals("account-123", result.getId());
    }

    @Test
    void accountService_should_throw_NotFoundException_when_getAccountDetails_with_invalid_accountId() {
        // Given
        GetAccountDetailsRequest request = new GetAccountDetailsRequest();
        request.setAccountId("invalid-account");

        when(accountRepository.findAccountById("invalid-account")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> accountService.getAccountDetails(request));
    }

    // ========== GET ACCOUNT BY IBAN TESTS ==========

    @Test
    void accountService_should_return_getAccountByIBAN_when_getAccountByIBAN_with_valid_iban() {
        // Given
        String iban = "TR123456789012345678901234";
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(testAccount));

        // When
        GetAccountByIBAN result = accountService.getAccountByIBAN(iban);

        // Then
        assertNotNull(result);
        assertEquals("account-123", result.getAccountId());
        assertEquals("Test Account", result.getAccountName());
        assertEquals("user-123", result.getUserId());
        assertEquals("John", result.getFirstName());
        assertEquals("Middle", result.getSecondName());
        assertEquals("Doe", result.getLastName());
        assertEquals("12*******01", result.getTckn());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals(1000.0, result.getBalance());
        assertEquals(Currency.TRY, result.getCurrency());
        assertEquals(AccountStatus.ACTIVE, result.getAccountStatus());
    }

    @Test
    void accountService_should_throw_NotFoundException_when_getAccountByIBAN_with_invalid_iban() {
        // Given
        String iban = "INVALID_IBAN";
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> accountService.getAccountByIBAN(iban));
    }

    @Test
    void accountService_should_return_masked_tckn_when_getAccountByIBAN_with_tckn() {
        // Given
        String iban = "TR123456789012345678901234";
        testUser.setTckn("12345678901");
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(testAccount));

        // When
        GetAccountByIBAN result = accountService.getAccountByIBAN(iban);

        // Then
        assertEquals("12*******01", result.getTckn());
    }

    @Test
    void accountService_should_return_null_tckn_when_getAccountByIBAN_with_null_tckn() {
        // Given
        String iban = "TR123456789012345678901234";
        testUser.setTckn(null);
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(testAccount));

        // When
        GetAccountByIBAN result = accountService.getAccountByIBAN(iban);

        // Then
        assertNull(result.getTckn());
    }

    // ========== GET ACCOUNT OWNER NAME TESTS ==========

    @Test
    void accountService_should_return_ownerName_when_getAccountOwnerName_with_valid_iban() {
        // Given
        String iban = "TR123456789012345678901234";
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(testAccount));

        // When
        GetAccountOwnerNameResponse result = accountService.getAccountOwnerName(iban);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Middle", result.getSecondName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void accountService_should_throw_NotFoundException_when_getAccountOwnerName_with_invalid_iban() {
        // Given
        String iban = "INVALID_IBAN";
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> accountService.getAccountOwnerName(iban));
    }

    // ========== UPDATE BALANCE TESTS ==========

    @Test
    void accountService_should_return_success_when_updateBalance_with_valid_iban() {
        // Given
        String iban = "TR123456789012345678901234";
        double amount = 500.0;
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(testAccount));

        // When
        BaseResponse result = accountService.updateBalance(iban, amount);

        // Then
        assertNotNull(result);
        assertTrue(result.getProcessMessage().contains("Balance updated successfully"));
        assertEquals(1500.0, testAccount.getBalance());
        verify(accountRepository).save(testAccount);
        verify(accountCacheService).updateAccount(testAccount);
    }

    @Test
    void accountService_should_throw_NotFoundException_when_updateBalance_with_invalid_iban() {
        // Given
        String iban = "INVALID_IBAN";
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> accountService.updateBalance(iban, 100.0));
    }

    @Test
    void accountService_should_return_success_when_updateBalance_with_negative_amount() {
        // Given
        String iban = "TR123456789012345678901234";
        double amount = -200.0;
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(testAccount));

        // When
        BaseResponse result = accountService.updateBalance(iban, amount);

        // Then
        assertEquals(800.0, testAccount.getBalance());
        verify(accountRepository).save(testAccount);
    }

    // ========== GET ACCOUNT PROFILE TESTS ==========

    @Test
    void accountService_should_return_accountProfile_when_getAccountProfile_with_valid_accountId() {
        // Given
        String accountId = "account-123";
        List<Card> cards = List.of(testCard);

        when(accountRepository.findAccountById(accountId)).thenReturn(Optional.of(testAccount));
        when(cardRepository.findByAccountId(accountId)).thenReturn(cards);

        // When
        AccountProfileResponse result = accountService.getAccountProfile(accountId);

        // Then
        assertNotNull(result);
        assertEquals("account-123", result.getAccountId());
        assertEquals("user-123", result.getUserId());
        assertEquals("TR123456789012345678901234", result.getIban());
        assertEquals("Test Account", result.getAccountName());
        assertEquals(750, result.getCreditScore());
        assertEquals(1000.0, result.getCurrentBalance());
        assertEquals(CardType.DEBIT.toString(), result.getCardType());
    }

    @Test
    void accountService_should_return_accountProfile_with_default_values_when_getAccountProfile_with_no_cards() {
        // Given
        String accountId = "account-123";
        when(accountRepository.findAccountById(accountId)).thenReturn(Optional.of(testAccount));
        when(cardRepository.findByAccountId(accountId)).thenReturn(Collections.emptyList());

        // When
        AccountProfileResponse result = accountService.getAccountProfile(accountId);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getCardAgeMonths());
        assertEquals("DEBIT", result.getCardType());
    }

    @Test
    void accountService_should_throw_NotFoundException_when_getAccountProfile_with_invalid_accountId() {
        // Given
        String accountId = "invalid-account";
        when(accountRepository.findAccountById(accountId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> accountService.getAccountProfile(accountId));
    }

    // ========== HOLD ACCOUNT TESTS ==========

    @Test
    void accountService_should_return_success_when_holdAccount_with_valid_accountId() {
        // Given
        String accountId = "account-123";
        when(accountRepository.findAccountById(accountId)).thenReturn(Optional.of(testAccount));

        // When
        BaseResponse result = accountService.holdAccount(accountId);

        // Then
        assertNotNull(result);
        assertEquals("Account has been put on hold due to suspicious activity", result.getProcessMessage());
        assertEquals(AccountStatus.HOLD, testAccount.getStatus());
        verify(accountRepository).save(testAccount);
        verify(accountCacheService).updateAccount(testAccount);
    }

    @Test
    void accountService_should_throw_NotFoundException_when_holdAccount_with_invalid_accountId() {
        // Given
        String accountId = "invalid-account";
        when(accountRepository.findAccountById(accountId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> accountService.holdAccount(accountId));
    }

    // ========== IS RECEIVER BLACKLISTED TESTS ==========

    @Test
    void accountService_should_return_true_when_isReceiverBlacklisted_with_blacklisted_iban() {
        // Given
        String iban = "TR123456789012345678901234";
        when(blacklistRepository.existsByIbanAndIsActiveTrue(iban)).thenReturn(true);

        // When
        Boolean result = accountService.isReceiverBlacklisted(iban);

        // Then
        assertTrue(result);
    }

    @Test
    void accountService_should_return_false_when_isReceiverBlacklisted_with_non_blacklisted_iban() {
        // Given
        String iban = "TR123456789012345678901234";
        when(blacklistRepository.existsByIbanAndIsActiveTrue(iban)).thenReturn(false);

        // When
        Boolean result = accountService.isReceiverBlacklisted(iban);

        // Then
        assertFalse(result);
    }

    // ========== VALIDATE ACCOUNT NOT BLOCKED TESTS ==========

    @Test
    void accountService_should_return_true_when_validateAccountNotBlocked_with_non_blocked_account() {
        // Given
        String iban = "TR123456789012345678901234";
        testAccount.setBlockedUntil(null);
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(testAccount));

        // When
        Boolean result = accountService.validateAccountNotBlocked(iban);

        // Then
        assertTrue(result);
    }

    @Test
    void accountService_should_return_false_when_validateAccountNotBlocked_with_blocked_account() {
        // Given
        String iban = "TR123456789012345678901234";
        testAccount.setBlockedUntil(LocalDateTime.now().plusHours(12));
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(testAccount));

        // When
        Boolean result = accountService.validateAccountNotBlocked(iban);

        // Then
        assertFalse(result);
    }

    @Test
    void accountService_should_return_true_when_validateAccountNotBlocked_with_expired_block() {
        // Given
        String iban = "TR123456789012345678901234";
        testAccount.setBlockedUntil(LocalDateTime.now().minusHours(1));
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(testAccount));

        // When
        Boolean result = accountService.validateAccountNotBlocked(iban);

        // Then
        assertTrue(result);
    }

    @Test
    void accountService_should_throw_NotFoundException_when_validateAccountNotBlocked_with_invalid_iban() {
        // Given
        String iban = "INVALID_IBAN";
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> accountService.validateAccountNotBlocked(iban));
    }

    // ========== INCREMENT FRAUD COUNT TESTS ==========

    @Test
    void accountService_should_return_success_when_incrementFraudCount_below_threshold() {
        // Given
        String accountId = "account-123";
        String reason = "Suspicious activity";
        testAccount.setPreviousFraudCount(0);
        when(accountRepository.findAccountById(accountId)).thenReturn(Optional.of(testAccount));

        // When
        BaseResponse result = accountService.incrementFraudCount(accountId, reason);

        // Then
        assertNotNull(result);
        assertTrue(result.getProcessMessage().contains("Fraud count incremented to 1"));
        assertEquals(1, testAccount.getPreviousFraudCount());
        assertTrue(testAccount.getPreviousFraudFlag());
        verify(accountRepository).save(testAccount);
    }

    @Test
    void accountService_should_return_blocked_message_when_incrementFraudCount_reaches_threshold() {
        // Given
        String accountId = "account-123";
        String reason = "Suspicious activity";
        testAccount.setPreviousFraudCount(2);
        when(accountRepository.findAccountById(accountId)).thenReturn(Optional.of(testAccount));

        // When
        BaseResponse result = accountService.incrementFraudCount(accountId, reason);

        // Then
        assertNotNull(result);
        assertTrue(result.getProcessMessage().contains("temporarily blocked"));
        assertEquals(0, testAccount.getPreviousFraudCount());
        assertEquals(AccountStatus.BLOCKED, testAccount.getStatus());
        assertNotNull(testAccount.getBlockedUntil());
        assertEquals(reason, testAccount.getBlockedReason());
        verify(accountRepository).save(testAccount);
    }

    @Test
    void accountService_should_throw_NotFoundException_when_incrementFraudCount_with_invalid_accountId() {
        // Given
        String accountId = "invalid-account";
        String reason = "Suspicious activity";
        when(accountRepository.findAccountById(accountId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> accountService.incrementFraudCount(accountId, reason));
    }

    @Test
    void accountService_should_return_success_when_incrementFraudCount_with_null_previous_count() {
        // Given
        String accountId = "account-123";
        String reason = "Suspicious activity";
        testAccount.setPreviousFraudCount(null);
        when(accountRepository.findAccountById(accountId)).thenReturn(Optional.of(testAccount));

        // When
        BaseResponse result = accountService.incrementFraudCount(accountId, reason);

        // Then
        assertNotNull(result);
        assertTrue(result.getProcessMessage().contains("Fraud count incremented to 1"));
        assertEquals(1, testAccount.getPreviousFraudCount());
    }

    // ========== CACHE UPDATE FAILURE TESTS ==========

    @Test
    void accountService_should_return_success_when_updateBalance_cache_update_fails() {
        // Given
        String iban = "TR123456789012345678901234";
        double amount = 500.0;
        when(accountRepository.findAccountByIban(iban)).thenReturn(Optional.of(testAccount));
        doThrow(new RuntimeException("Cache error")).when(accountCacheService).updateAccount(any(Account.class));

        // When
        BaseResponse result = accountService.updateBalance(iban, amount);

        // Then
        assertNotNull(result);
        assertTrue(result.getProcessMessage().contains("Balance updated successfully"));
        verify(accountRepository).save(testAccount);
    }

    @Test
    void accountService_should_return_success_when_holdAccount_cache_update_fails() {
        // Given
        String accountId = "account-123";
        when(accountRepository.findAccountById(accountId)).thenReturn(Optional.of(testAccount));
        doThrow(new RuntimeException("Cache error")).when(accountCacheService).updateAccount(any(Account.class));

        // When
        BaseResponse result = accountService.holdAccount(accountId);

        // Then
        assertNotNull(result);
        assertEquals(AccountStatus.HOLD, testAccount.getStatus());
        verify(accountRepository).save(testAccount);
    }
}

