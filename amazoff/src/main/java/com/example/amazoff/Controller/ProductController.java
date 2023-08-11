package com.example.amazoff.Controller;

import com.example.amazoff.Dto.Request.ProductRequestDto;
import com.example.amazoff.Dto.Request.SellerRequestDto;
import com.example.amazoff.Dto.Response.ProductResponseDto;
import com.example.amazoff.Dto.SellerResponseDto;
import com.example.amazoff.Enum.ProductCategory;
import com.example.amazoff.Exception.SellerNotFound;
import com.example.amazoff.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/addproduct")
    public ResponseEntity addProduct(@RequestBody ProductRequestDto productRequestDto){
        try {
            ProductResponseDto productResponseDto = productService.addProduct(productRequestDto);
            return new ResponseEntity(productResponseDto, HttpStatus.ACCEPTED);
        }catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/filterbycategoryandprice")
    public ResponseEntity filterProductsByCategoryAndPrice(@RequestParam("price")int price, @RequestParam("category")ProductCategory productCategory){
       List<ProductResponseDto> productResponseDtoList=productService.filterproductsByCategoryAndPrice(price,productCategory);
       return new ResponseEntity(productResponseDtoList,HttpStatus.FOUND);
    }
    @GetMapping("/allproductsofseller")
    public ResponseEntity getAllProductsOfSeller(@RequestParam("email")String emailId,@RequestParam("password")String password){
       try{
        List<ProductResponseDto>productResponseDtoList=productService.getAllProductsOfSeller(emailId,password);
        return new ResponseEntity(productResponseDtoList,HttpStatus.ACCEPTED);
    }catch (Exception e){
           return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
       }
    }
}
