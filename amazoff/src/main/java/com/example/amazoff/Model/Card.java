package com.example.amazoff.Model;

import com.example.amazoff.Enum.CardType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(unique = true)
    String cardNo;

    int cvv;
    Date validTill;
    @Enumerated(EnumType.STRING)
    CardType cardType;
    @ManyToOne
    Customer customer;

}
