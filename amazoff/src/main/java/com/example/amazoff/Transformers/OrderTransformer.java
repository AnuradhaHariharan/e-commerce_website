package com.example.amazoff.Transformers;

import com.example.amazoff.Dto.Response.ItemResponseDto;
import com.example.amazoff.Dto.Response.OrderResponseDto;
import com.example.amazoff.Model.Item;
import com.example.amazoff.Model.OrderEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderTransformer {

    public static OrderResponseDto entityToResponseDto(OrderEntity orderEntity){
        List<ItemResponseDto> itemResponseDtoList=new ArrayList<>();
        for(Item item : orderEntity.getItemList()){
            itemResponseDtoList.add(ItemTransformer.EntityToResponseDto(item));
        }
        return OrderResponseDto.builder()
                .orderId(orderEntity.getOrderId())
                .date(orderEntity.getDate())
                .orderTotal(orderEntity.getOrderTotal())
                .cardUsed(orderEntity.getCardUsed())
                .customerName(orderEntity.getCustomer().getName())
                .itemResponseDtoList(itemResponseDtoList).build();
    }
}
