package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.api.client.EmailServiceClient;
import com.modernbank.account_service.api.request.card.CardApprovalEmailRequest;
import com.modernbank.account_service.api.request.card.CardPendingEmailRequest;
import com.modernbank.account_service.api.request.card.CardRejectionEmailRequest;
import com.modernbank.account_service.entity.Account;
import com.modernbank.account_service.entity.Card;
import com.modernbank.account_service.entity.User;
import com.modernbank.account_service.exception.BusinessException;
import com.modernbank.account_service.exception.NotFoundException;
import com.modernbank.account_service.model.CardModel;
import com.modernbank.account_service.model.CreateCardModel;
import com.modernbank.account_service.model.enums.CardNetwork;
import com.modernbank.account_service.model.enums.CardStatus;
import com.modernbank.account_service.model.enums.CardType;
import com.modernbank.account_service.repository.AccountRepository;
import com.modernbank.account_service.repository.CardRepository;
import com.modernbank.account_service.rest.service.CardService;
import com.modernbank.account_service.rest.service.CvvEncryptionService;
import com.modernbank.account_service.util.CardNumberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    private final AccountRepository accountRepository;

    private final CardNumberService cardNumberService;

    private final PasswordEncoder passwordEncoder;

    private final CvvEncryptionService cvvEncryptionService;

    private final EmailServiceClient emailServiceClient;

    private static final SecureRandom random = new SecureRandom();

    @Override
    public List<CardModel> getCardsForAccount(String accountId, String userId) {
        List<Card> cards = cardRepository.getCardsByUserIdAndAccountId(userId, accountId)
                .orElseThrow(() -> new NotFoundException(
                        "No cards found for user: " + userId + " and account: " + accountId));

        return cards.stream()
                .filter(card -> card.getStatus() != CardStatus.CANCELLED)
                .map(this::mapCardToModel)
                .collect(Collectors.toList());
    }

    private CardModel mapCardToModel(Card card) {
        boolean isPending = card.getStatus() == CardStatus.PENDING_APPROVAL;
        if(card.getIsEmailNotified().equals(Boolean.FALSE)){
            String rawCvv = cvvEncryptionService.decrypt(card.getRawCvvEncrypted());
            sendCardApprovalEmail(card, rawCvv);
        }

        return CardModel.builder()
                .id(card.getId())
                .cardNumber(isPending ? "Onay Bekleniyor" : maskCardNumber(card.getCardNumber()))
                .cardHolderName(card.getCardHolderName())
                .expiryDate(isPending ? null : card.getExpiryDate())
                .network(isPending ? null : card.getNetwork())
                .type(card.getType())
                .status(card.getStatus())
                .cvv(isPending ? "---" : "***")
                .pendingApproval(isPending)
                .build();
    }

    @Override
    @Transactional
    public void createCardForAccount(CreateCardModel createCardModel) {
        Account account = getOrFetchAccount(createCardModel);
        User user = account.getUser();

        log.debug("createCardForAccount - start: accountId={}, userId={}",
                account.getId(), user.getId());

        int currentCardCount = account.getCards() != null ? account.getCards().size() : 0;

        if (currentCardCount >= 3) {
            throw new BusinessException("Bir hesaba en fazla 3 kart tanımlanabilir.");
        }

        String cardHolderName = buildCardHolderName(user);

        Card card = createCardProcess(CreateCardModel.builder()
                .cardNetwork(createCardModel.getCardNetwork())
                .cardType(createCardModel.getCardType())
                .cardHolderName(cardHolderName)
                .account(account)
                .build());

        if (currentCardCount == 0) {
            approveCard(card.getId(), "system-auto-approval");
            log.info("createCardForAccount - first card auto-approved: cardId={}, accountId={}",
                    card.getId(), account.getId());
            return;
        }

        sendCardPendingEmail(user, card, cardHolderName);

        log.info("createCardForAccount - card created with PENDING status: cardId={}, accountId={}",
                card.getId(), account.getId());
    }

    private void sendCardPendingEmail(User user, Card card, String cardHolderName) {
        try {
            emailServiceClient.sendCardPendingNotification(CardPendingEmailRequest.builder()
                    .applicationDate(LocalDateTime.now().toString())
                    .userEmail(user.getEmail())
                    .cardType(card.getType().toString())
                    .cardHolderName(cardHolderName)
                    .build()
            );
            log.info("createCardForAccount - email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("createCardForAccount - failed to send email notification for cardId={}", card.getId(), e);
        }
    }

    @Override
    @Transactional
    public void approveCard(String cardId, String approvedBy) {
        log.info("Approving card: cardId={}, approvedBy={}", cardId, approvedBy);

        Card card = findCardById(cardId);
        validatePendingApprovalStatus(card);
        String rawCvv = cvvEncryptionService.decrypt(card.getRawCvvEncrypted());
        activateCard(card, approvedBy);
        setCardLimits(card);

        sendCardApprovalEmail(card, rawCvv);

        log.info("Card approved successfully: cardId={}", cardId);
    }

    private Card findCardById(String cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card not found: " + cardId));
    }

    private void validatePendingApprovalStatus(Card card) {
        if (card.getStatus() != CardStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Card is not in pending approval status");
        }
    }

    private void activateCard(Card card, String approvedBy) {
        card.setStatus(CardStatus.ACTIVE);
        card.setApprovedDate(LocalDateTime.now());
        card.setApprovedBy(approvedBy);
        card.setUpdatedDate(LocalDateTime.now());
        card.setRawCvvEncrypted(null); cardRepository.save(card);
    }

    private void setCardLimits(Card card) {
        double balance = card.getAccount().getBalance();
        double limitMultiplier = card.getType() == CardType.CREDIT ? 3.0 : 1.0; //TODO: Bunu konfigürasyondan al

        card.setLimitAmount(balance * limitMultiplier);
        card.setAvailableAmount(card.getLimitAmount());
        cardRepository.save(card);
    }

    private void sendCardApprovalEmail(Card card, String rawCvv) {
        String userEmail = card.getAccount().getUser().getEmail();
        try {
            emailServiceClient.sendCardApprovalNotification(CardApprovalEmailRequest.builder()
                    .userEmail(userEmail)
                    .cardHolderName(card.getCardHolderName())
                    .cardNumber(maskCardNumber(card.getCardNumber()))
                    .cvv(rawCvv)
                    .expiryDate(card.getExpiryDate().toLocalDate().toString())
                    .cardType(card.getType().toString())
                    .cardNetwork(card.getNetwork().toString())
                    .creditLimit(card.getLimitAmount())
                    .build()
            );
            card.setIsEmailNotified(true);
            log.info("Card approval email sent to: {}", userEmail);
        } catch (Exception e) {
            card.setIsEmailNotified(false);
            log.error("Error while sending card approval email: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public void rejectCard(String cardId, String rejectedBy, String reason) {
        log.info("Rejecting card: cardId={}, rejectedBy={}", cardId, rejectedBy);
        Card card = findCardById(cardId);
        validatePendingApprovalStatus(card);
        rejectCardProcess(card);

        sendCardRejectionEmail(card, reason);
        log.info("Card rejected: cardId={}, reason={}", cardId, reason);
    }

    private void rejectCardProcess(Card card) {
        card.setStatus(CardStatus.CANCELLED);
        card.setUpdatedDate(LocalDateTime.now());
        card.setRawCvvEncrypted(null);
        cardRepository.save(card);
    }

    private void sendCardRejectionEmail(Card card, String reason) {
        String userEmail = card.getAccount().getUser().getEmail();
        try {
            emailServiceClient.sendCardRejectionNotification(CardRejectionEmailRequest.builder()
                    .userEmail(userEmail) .cardHolderName(card.getCardHolderName())
                    .cardType(card.getType().toString())
                    .applicationDate(card.getCreatedDate().toString())
                    .rejectionReason(reason)
                    .build()
            );
            log.info("Card rejection email sent to: {}", userEmail);
        } catch (Exception e) {
            log.error("Error while sending card rejection email: {}", e.getMessage());
        }
    }

    @Override
    public void blockCard(String cardId, String userId) {
        log.info("Blocking card with id: {} for user with id: {}", cardId, userId);
        Card card = cardRepository.findByCardNumberAndUserId(cardId, userId)
                .orElseThrow(() -> new NotFoundException("Card not found with id: " + cardId + " for user with id: " + userId));

        card.setStatus(CardStatus.BLOCKED);
        card.setUpdatedDate(LocalDateTime.now());
        cardRepository.save(card);
    }

    @Override
    public void unblockCard(String cardId, String userId) {
        log.info("Unblocking card with id: {} for user with id: {}", cardId, userId);
        Card card = cardRepository.findByCardNumberAndUserId(cardId, userId)
                .orElseThrow(() -> new NotFoundException("Card not found with id: " + cardId + " for user with id: " + userId));

        card.setStatus(CardStatus.ACTIVE);
        card.setUpdatedDate(LocalDateTime.now());
        cardRepository.save(card);
    }

    @Override
    public void deleteCard(String cardId, String userId) {
        log.info("Deleting card with id: {} for user with id: {}", cardId, userId);
        Card card = cardRepository.findByCardNumberAndUserId(cardId, userId)
                .orElseThrow(() -> new NotFoundException("Card not found with id: " + cardId + " for user with id: " + userId));

        card.setStatus(CardStatus.CANCELLED);
        card.setUpdatedDate(LocalDateTime.now());
        cardRepository.save(card);
    }

    private Card createCardProcess(CreateCardModel model) {
        long start = System.currentTimeMillis();
        log.debug("createCardProcess - start");

        String rawCvv = generateCvv();
        String hashedCvv = passwordEncoder.encode(rawCvv);
        String encryptedCvv = cvvEncryptionService.encrypt(rawCvv);

        Card card = Card.builder()
                .cardHolderName(model.getCardHolderName())
                .type(CardType.valueOf(model.getCardType()))
                .network(CardNetwork.valueOf(model.getCardNetwork()))
                .cardNumber(cardNumberService.generateUniqueCardNumber(
                        CardNetwork.valueOf(model.getCardNetwork())))
                .expiryDate(calculateExpiryDate())
                .status(CardStatus.PENDING_APPROVAL)
                .account(model.getAccount())
                .cvv(hashedCvv)
                .rawCvvEncrypted(encryptedCvv)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Card saved = cardRepository.save(card);
        log.debug("createCardProcess - finished in {}ms", System.currentTimeMillis() - start);
        return saved;
    }

    private Account getOrFetchAccount(CreateCardModel model) {
        if (model.getAccount() != null) {
            return model.getAccount();
        }
        return accountRepository.findById(model.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found: " + model.getAccountId()));
    }

    private String buildCardHolderName(User user) {
        return Stream.of(user.getFirstName(), user.getLastName())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(" "));
    }


    private String generateCvv() {
        return String.valueOf(100 + random.nextInt(900));
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDate.now().plusYears(3).atStartOfDay();
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}