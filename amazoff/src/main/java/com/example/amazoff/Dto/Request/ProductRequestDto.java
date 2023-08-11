package com.example.amazoff.Dto.Request;

import com.example.amazoff.Enum.ProductCategory;
import com.example.amazoff.Enum.ProductStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequestDto {
    String emailId;
    String password;
    String productName;
    int productPrice;
    int availableQuantity;
    ProductCategory productCategory;

}
