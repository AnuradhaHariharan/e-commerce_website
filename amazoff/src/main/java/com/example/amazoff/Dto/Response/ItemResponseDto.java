package com.example.amazoff.Dto.Response;

import com.example.amazoff.Enum.ProductCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemResponseDto {
    int quantityAdded;
    String itemName;
    int itemPrice;
    ProductCategory productCategory;

}
