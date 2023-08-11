package com.example.amazoff.Transformers;

import com.example.amazoff.Dto.Request.SellerRequestDto;
import com.example.amazoff.Dto.SellerResponseDto;
import com.example.amazoff.Model.Seller;

public class SellerTransformer {

    public static Seller requestDtoToEntity(SellerRequestDto sellerRequestDto){
        return Seller.builder()
                .age(sellerRequestDto.getAge())
                .panId(sellerRequestDto.getPanId())
                .name(sellerRequestDto.getName())
                .gender(sellerRequestDto.getGender())
                .emailId(sellerRequestDto.getEmailId())
                .password(sellerRequestDto.getPassword()).build();

    }
    public static SellerResponseDto EntityToResponseDto(Seller seller){
        return SellerResponseDto.builder().emailId(seller.getEmailId())
                .name(seller.getName())
                .build();
    }
}
