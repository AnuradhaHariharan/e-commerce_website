package com.example.amazoff.Service;

import com.example.amazoff.Dto.Request.ProductRequestDto;
import com.example.amazoff.Dto.Request.SellerRequestDto;
import com.example.amazoff.Dto.Response.ProductResponseDto;
import com.example.amazoff.Dto.SellerResponseDto;
import com.example.amazoff.Enum.ProductCategory;
import com.example.amazoff.Exception.IncorrectPasswordException;
import com.example.amazoff.Exception.SellerNotFound;
import com.example.amazoff.Model.Product;
import com.example.amazoff.Model.Seller;
import com.example.amazoff.Repository.ProductRepository;
import com.example.amazoff.Repository.SellerRepository;
import com.example.amazoff.Transformers.ProductTransformer;
import com.example.amazoff.Utility.EmailService;
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
    @Autowired
    EmailService emailService;
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        Seller seller=sellerRepository.getByEmailId(productRequestDto.getEmailId());
        if(seller==null){
            throw new SellerNotFound("Cannot find Seller");
        }
        if(!seller.getPassword().equals(productRequestDto.getPassword())){
            throw new IncorrectPasswordException("Incorrect password.");
        }
        Product product= ProductTransformer.RequestDtoToEntity(productRequestDto);
        product.setSeller(seller);
        seller.getProducts().add(product);
        Seller savedSeller=sellerRepository.save(seller);

List<Product>productList=savedSeller.getProducts();
Product latestProduct=productList.get(productList.size()-1);
        ProductResponseDto productResponseDto=ProductTransformer.entityToResponseDto(product);
       sendProductAddedEmail(seller,productResponseDto);
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
    public List<ProductResponseDto> getAllProductsOfSeller(String emailId, String password) {
        Seller seller = sellerRepository.getByEmailId(emailId);
        if (seller == null) {
            throw new SellerNotFound("Seller not found.");
        }
        if (!seller.getPassword().equals(password)) {
            throw new IncorrectPasswordException("Incorrect password.");
        }

        List<Product> productList = productRepository.findBySeller(seller);
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Product product : productList) {
            ProductResponseDto productResponseDto = ProductTransformer.entityToResponseDto(product);
            productResponseDtoList.add(productResponseDto);
        }

        return productResponseDtoList;
    }

    private void sendProductAddedEmail(Seller seller, ProductResponseDto productResponseDto) {
        String emailContent = "Dear " + seller.getName() + ",\n\n" +
                "We are pleased to inform you that a new product has been added to your inventory:\n\n" +
                "Product Name: " + productResponseDto.getProductName() + "\n" +
                "Price: " + productResponseDto.getProductPrice() + "\n" +
                "Category: " + productResponseDto.getProductCategory() + "\n\n" +
                "Thank you for using our platform!\n" +
                "The Amazoff Team";

        emailService.sendEmail(seller.getEmailId(), "New Product Added", emailContent);
    }


}

