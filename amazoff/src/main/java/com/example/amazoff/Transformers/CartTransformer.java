package com.example.amazoff.Transformers;

import com.example.amazoff.Dto.Response.CartResponseDto;
import com.example.amazoff.Dto.Response.ItemResponseDto;
import com.example.amazoff.Model.Cart;
import com.example.amazoff.Model.Item;

import java.util.ArrayList;
import java.util.List;

public class CartTransformer {
    public static CartResponseDto entityToResponseDto(Cart cart){
        List<ItemResponseDto> itemResponseDtoList=new ArrayList<>();
        for(Item item: cart.getItemList()){
            itemResponseDtoList.add(ItemTransformer.EntityToResponseDto(item));
        }
        return CartResponseDto.builder().cartTotal(cart.getCartTotal())
                .customerName(cart.getCustomer().getName())
                .itemList(itemResponseDtoList).build();
    }
}
