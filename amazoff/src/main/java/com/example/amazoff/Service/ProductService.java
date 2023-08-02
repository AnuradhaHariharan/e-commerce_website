package com.example.amazoff.Service;

import com.example.amazoff.Dto.Request.ProductRequestDto;
import com.example.amazoff.Dto.Request.SellerRequestDto;
import com.example.amazoff.Dto.Response.ProductResponseDto;
import com.example.amazoff.Dto.SellerResponseDto;
import com.example.amazoff.Enum.ProductCategory;
import com.example.amazoff.Exception.SellerNotFound;
import com.example.amazoff.Model.Product;
import com.example.amazoff.Model.Seller;
import com.example.amazoff.Repository.ProductRepository;
import com.example.amazoff.Repository.SellerRepository;
import com.example.amazoff.Transformers.ProductTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    SellerRepository sellerRepository;
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        Seller seller=sellerRepository.getByEmailId(productRequestDto.getEmailId());
        if(seller==null){
            throw new SellerNotFound("Cannot find Seller");
        }
        Product product= ProductTransformer.RequestDtoToEntity(productRequestDto);
        product.setSeller(seller);
        seller.getProducts().add(product);
        Seller savedSeller=sellerRepository.save(seller);

List<Product>productList=savedSeller.getProducts();
Product latestProduct=productList.get(productList.size()-1);
        ProductResponseDto productResponseDto=ProductTransformer.entityToResponseDto(product);

         return productResponseDto;
    }

    public List<ProductResponseDto> filterproductsByCategoryAndPrice(int price, ProductCategory productCategory){
        List<Product>productList=productRepository.filterProductsByCategoryAndPrice(price,productCategory);
        List<ProductResponseDto> productResponseDtoList=new ArrayList<>();
        for(Product product:productList ){
            productResponseDtoList.add(ProductTransformer.entityToResponseDto(product));
        }
        return productResponseDtoList;
    }
}
