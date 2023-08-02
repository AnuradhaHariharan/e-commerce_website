package com.example.amazoff.Dto.Response;

import com.example.amazoff.Enum.ProductCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponseDto {
    int sellerId;
    String sellerName;
    String productName;
    int productPrice;
    int availableQuantity;
    ProductCategory productCategory;
}
