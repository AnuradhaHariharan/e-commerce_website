package com.example.amazoff.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int cartTotal;
    @OneToOne
    @JoinColumn
    Customer customer;

    @OneToMany(mappedBy="cart",cascade=CascadeType.ALL)
    List<Item> itemList=new ArrayList<>();
}
