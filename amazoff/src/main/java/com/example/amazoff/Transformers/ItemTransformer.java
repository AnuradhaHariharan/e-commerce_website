package com.example.amazoff.Transformers;

import com.example.amazoff.Dto.Request.ItemRequestDto;
import com.example.amazoff.Dto.Response.ItemResponseDto;
import com.example.amazoff.Model.Item;

public class ItemTransformer {
    public static Item itemRequestDtoToEntity(int requiredQuantity){
       return Item.builder().requiredQuantity(requiredQuantity).build();
    }
    public static ItemResponseDto EntityToResponseDto(Item item){
        return ItemResponseDto.builder().itemPrice(item.getProduct().getProductPrice())
                .itemName(item.getProduct().getProductName())
                .productCategory(item.getProduct().getProductCategory())
                .quantityAdded(item.getRequiredQuantity()).build();

    }
}
