package com.example.amazoff.Model;

import com.example.amazoff.Enum.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    @Column(unique = true)
    String emailId;
    @Column(unique = true)
    String mobNo;
    Gender gender;

    @OneToOne(mappedBy="customer",cascade=CascadeType.ALL)
    Cart cart;
    @OneToMany(mappedBy="customer",cascade=CascadeType.ALL)
    Card card;
}
