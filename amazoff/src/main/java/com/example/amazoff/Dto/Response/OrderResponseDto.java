package com.example.amazoff.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponseDto {
    String customerName;
    String orderId;
    Date date;
    String cardUsed;
    int orderTotal;
    List<ItemResponseDto> itemResponseDtoList;
}

