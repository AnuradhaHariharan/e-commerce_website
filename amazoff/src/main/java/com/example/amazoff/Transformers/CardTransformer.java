package com.example.amazoff.Transformers;

import com.example.amazoff.Dto.Request.CardRequestDto;
import com.example.amazoff.Dto.Response.CardResponseDto;
import com.example.amazoff.Model.Card;

public class CardTransformer {

    public static Card requestDtoToEntity(CardRequestDto cardRequestDto){
        return Card.builder().cardNo(cardRequestDto.getCardNo())
                .cardType(cardRequestDto.getCardType())
                .cvv(cardRequestDto.getCvv())
                .validTill(cardRequestDto.getValidTill())
                .build();
    }

    public static CardResponseDto entityToRequestDto(Card card){
        return CardResponseDto.builder().cardType(card.getCardType())
                .validTill(card.getValidTill())
                .build();


    }
}
