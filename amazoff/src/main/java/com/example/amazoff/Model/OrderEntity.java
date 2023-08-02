package com.example.amazoff.Model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "order_entity")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String OrderId;
    @CreationTimestamp
     Date date;

    String cardUsed;
    int OrderTotal;

    @OneToMany(mappedBy="orderEntity",cascade=CascadeType.ALL)
    List<Item> itemList=new ArrayList<>();

    @ManyToOne
            @JoinColumn
    Customer customer;
}
