package com.example.amazoff.Transformers;

import com.example.amazoff.Dto.Request.ProductRequestDto;
import com.example.amazoff.Dto.Response.ProductResponseDto;
import com.example.amazoff.Enum.ProductStatus;
import com.example.amazoff.Model.Product;

public class ProductTransformer {

    public static  Product RequestDtoToEntity(ProductRequestDto productRequestDto){
      return Product.builder().productCategory(productRequestDto.getProductCategory())
              .availableQuantity(productRequestDto.getAvailableQuantity())
              .productName(productRequestDto.getProductName())
              .productPrice(productRequestDto.getProductPrice())
              .productStatus(ProductStatus.AVAILABLE)
              .build();
    }
    public static ProductResponseDto entityToResponseDto(Product product){
       return ProductResponseDto.builder().productCategory(product.getProductCategory())
               .availableQuantity(product.getAvailableQuantity())
               .productName(product.getProductName())
               .productPrice(product.getProductPrice())
               .sellerId(product.getSeller().getId())
               .sellerName(product.getSeller().getName()).build();

    }
}
