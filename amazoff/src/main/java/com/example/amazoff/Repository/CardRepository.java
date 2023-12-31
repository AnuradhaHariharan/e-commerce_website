package com.example.amazoff.Repository;

import com.example.amazoff.Model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository <Card,Integer>{
    Card findByCardNo(String cardUsed);
}
