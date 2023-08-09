package com.example.amazoff.Dto.Response;

import com.example.amazoff.Enum.CardType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardResponseDto {
      String cardNo;
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      Date validTill;
      CardType cardType;
}
