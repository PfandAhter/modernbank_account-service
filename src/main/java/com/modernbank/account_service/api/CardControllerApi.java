package com.modernbank.account_service.api;

import com.modernbank.account_service.api.request.card.*;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.CardListResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CardControllerApi {

    @PostMapping(path = "/create")
    BaseResponse createCard(@RequestBody CreateCardRequest createCardRequest);

    @PostMapping(path = "/block")
    BaseResponse blockCard(@RequestBody BlockCardRequest blockCardRequest);

    @PostMapping(path = "/list")
    CardListResponse getCards(@RequestBody GetCardListRequest getCardListRequest);

    @PostMapping(path = "/admin/approve")
    BaseResponse approveCardCreation(@RequestBody ApproveCardRequest approveCardRequest);

    @PostMapping(path = "/admin/reject")
    BaseResponse rejectCardCreation(@RequestBody RejectCardRequest rejectCardRequest);
}