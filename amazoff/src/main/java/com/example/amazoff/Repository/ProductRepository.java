package com.example.amazoff.Repository;

import com.example.amazoff.Enum.ProductCategory;
import com.example.amazoff.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("select p from Product p where p.productPrice >= :price and p.productCategory = :category")
    public List<Product> filterProductsByCategoryAndPrice(int price, ProductCategory category);
}
