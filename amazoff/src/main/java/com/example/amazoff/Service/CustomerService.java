package com.example.amazoff.Service;

import com.example.amazoff.Dto.CustomerResponseDto;
import com.example.amazoff.Dto.Request.CustomerRequestDto;
import com.example.amazoff.Model.Cart;
import com.example.amazoff.Model.Customer;
import com.example.amazoff.Repository.CustomerRepository;
import com.example.amazoff.Transformers.CustomerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto) {
        Customer customer =CustomerTransformer.resquestDtoToCustomer(customerRequestDto);

        Cart cart = Cart.builder().cartTotal(0).customer(customer).build();
        customer.setCart(cart);
        customerRepository.save(customer);

        CustomerResponseDto customerResponseDto= CustomerTransformer.customerToCustomerResponseDto(customer);
        return customerResponseDto;
    }
}