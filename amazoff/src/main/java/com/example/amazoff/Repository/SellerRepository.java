package com.example.amazoff.Repository;

import com.example.amazoff.Model.Seller;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Integer> {
    Seller getByEmailId(String emailId);
}
