package com.example.amazoff.Repository;

import com.example.amazoff.Model.Customer;
import com.example.amazoff.Model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderEntityRepository extends JpaRepository<OrderEntity,Integer> {
    List<OrderEntity> findByCustomer(Customer customer);
}
