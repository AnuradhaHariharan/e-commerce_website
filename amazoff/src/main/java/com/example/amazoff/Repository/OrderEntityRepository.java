package com.example.amazoff.Repository;

import com.example.amazoff.Model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntityRepository extends JpaRepository<OrderEntity,Integer> {
}
