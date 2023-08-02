package com.example.amazoff.Service;

import com.example.amazoff.Dto.Request.SellerRequestDto;
import com.example.amazoff.Dto.SellerResponseDto;
import com.example.amazoff.Model.Seller;
import com.example.amazoff.Repository.SellerRepository;
import com.example.amazoff.Transformers.SellerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SellerService {
@Autowired
    SellerRepository sellerRepository;
    public SellerResponseDto addSeller(SellerRequestDto sellerRequestDto) {
        Seller seller= SellerTransformer.requestDtoToEntity(sellerRequestDto);
        sellerRepository.save(seller);

        return SellerTransformer.EntityToResponseDto(seller);
    }
}
