package com.example.amazoff.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponseDto {
    String customerName;
    int cartTotal;
    List<ItemResponseDto> itemList;
}
