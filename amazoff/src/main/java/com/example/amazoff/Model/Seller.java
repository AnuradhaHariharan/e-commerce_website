package com.example.amazoff.Model;

import com.example.amazoff.Enum.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "seller")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    int age;
    Gender gender;
    @Column(unique = true,nullable = false)
    String emailId;
    @Column(unique = true,nullable = false)
    String panId;
    @OneToMany(mappedBy="seller",cascade=CascadeType.ALL)
    List<Product>products=new ArrayList<>();
}
