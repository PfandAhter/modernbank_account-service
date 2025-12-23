package com.modernbank.account_service.rest.service;

import com.modernbank.account_service.model.CardModel;
import com.modernbank.account_service.model.CreateCardModel;

import java.util.List;

public interface CardService {
    List<CardModel> getCardsForAccount(String accountId, String userId);

    void createCardForAccount(CreateCardModel createCardModel);

    void blockCard(String cardId, String userId);

    void unblockCard(String cardId, String userId);

    void deleteCard(String cardId, String userId);

    void approveCard(String cardId, String approvedBy);

    void rejectCard(String cardId, String rejectedBy, String reason);
}