package com.example.amazoff.Model;

import com.example.amazoff.Enum.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "customer")
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    int age;

    @Enumerated(EnumType.STRING)
    Gender gender;


    @Column(unique = true,nullable = false)
    String emailId;

    @Column(nullable = false)
    String password;

    @Column(unique = true,nullable = false)
    String mobNo;

    String resetOtp; // The generated OTP
    LocalDateTime resetOtpExpiry;

    @OneToOne(mappedBy="customer",cascade=CascadeType.ALL)
    Cart cart;

    @OneToMany(mappedBy="customer",cascade=CascadeType.ALL)
    List<Card>cards=new ArrayList<>();

    @OneToMany(mappedBy="customer",cascade=CascadeType.ALL)
    List<OrderEntity>order=new ArrayList<>();
}
