package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.api.CardControllerApi;
import com.modernbank.account_service.api.dto.CardDTO;
import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.api.request.card.*;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.CardListResponse;
import com.modernbank.account_service.model.CreateCardModel;
import com.modernbank.account_service.rest.service.CardService;
import com.modernbank.account_service.rest.service.MapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/card")
public class CardServiceController implements CardControllerApi {

    private final CardService cardService;

    private final MapperService mapperService;

    @Override
    public BaseResponse createCard(CreateCardRequest createCardRequest) {
        cardService.createCardForAccount(CreateCardModel.builder()
                .accountId(createCardRequest.getAccountId())
                .cardNetwork(createCardRequest.getCardNetwork())
                .cardType(createCardRequest.getCardType())
                .build());
        return new BaseResponse("Kart başarıyla oluşturuldu.");
    }

    @Override
    public BaseResponse blockCard(BlockCardRequest blockCardRequest) {
        cardService.blockCard(blockCardRequest.getCardId(), blockCardRequest.getUserId());
        return new BaseResponse("Kart başarıyla engellendi.");
    }

    @Override
    public CardListResponse getCards(GetCardListRequest request) {
        return new CardListResponse(mapperService.map(
                cardService.getCardsForAccount(request.getAccountId(), request.getUserId()),
                CardDTO.class
        ));
    }

    @Override
    public BaseResponse approveCardCreation(ApproveCardRequest approveCardRequest) {
        cardService.approveCard(approveCardRequest.getCardId(), approveCardRequest.getUserId());
        return new BaseResponse("Kart "+ approveCardRequest.getCardId() + " başarıyla onaylandı.");
    }

    @Override
    public BaseResponse rejectCardCreation(RejectCardRequest rejectCardRequest) {
        cardService.rejectCard(rejectCardRequest.getCardId(), rejectCardRequest.getUserId(), rejectCardRequest.getReason());
        return new BaseResponse("Kart "+ rejectCardRequest.getCardId() + " başarıyla reddedildi.");
    }
}