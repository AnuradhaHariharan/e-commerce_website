package com.example.amazoff.Service;

import com.example.amazoff.Dto.Request.ItemRequestDto;
import com.example.amazoff.Dto.Response.ItemResponseDto;
import com.example.amazoff.Exception.CustomerNotFound;
import com.example.amazoff.Exception.InsufficientQuantityException;
import com.example.amazoff.Exception.ProductNotFound;
import com.example.amazoff.Model.Cart;
import com.example.amazoff.Model.Customer;
import com.example.amazoff.Model.Item;
import com.example.amazoff.Model.Product;
import com.example.amazoff.Repository.CustomerRepository;
import com.example.amazoff.Repository.ProductRepository;
import com.example.amazoff.Transformers.ItemTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;
    public Item createItem(ItemRequestDto itemRequestDto) {
        Customer customer=customerRepository.getByEmailId(itemRequestDto.getCustomerEmail());
        if(customer==null){
            throw new CustomerNotFound("Account not found");
        }
        Optional <Product> optionalProduct=productRepository.findById(itemRequestDto.getProductId());
        if(!optionalProduct.isPresent()){
            throw new ProductNotFound("Product not found");
        }
        Product product=optionalProduct.get();
        if(product.getAvailableQuantity()< itemRequestDto.getRequiredQuantity()){
            throw new InsufficientQuantityException("Sorry! Required Quantity Not Available.");
        }
   Item item= ItemTransformer.itemRequestDtoToEntity(itemRequestDto.getRequiredQuantity());
        item.setProduct(product);
        return item;

    }
}
