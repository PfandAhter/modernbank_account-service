package com.modernbank.account_service.api.response;

import com.modernbank.account_service.api.dto.CardDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CardListResponse extends BaseResponse{
    private List<CardDTO> carts;
}