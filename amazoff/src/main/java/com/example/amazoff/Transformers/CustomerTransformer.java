package com.example.amazoff.Transformers;

import com.example.amazoff.Dto.CustomerResponseDto;
import com.example.amazoff.Dto.Request.CustomerRequestDto;
import com.example.amazoff.Model.Customer;

public class CustomerTransformer {
    // keeping this method static since there is no attributes and we only need method.
    public static Customer resquestDtoToCustomer(CustomerRequestDto customerRequestDto) {
        return Customer.builder().name(customerRequestDto.getName()).mobNo(customerRequestDto.getMobNo()).emailId(customerRequestDto.getEmailId()).gender(customerRequestDto.getGender()).age(customerRequestDto.getAge()).build();

    }

    public static CustomerResponseDto customerToCustomerResponseDto(Customer customer) {
        return CustomerResponseDto.builder().emailId(customer.getEmailId()).mobNo(customer.getMobNo()).name(customer.getName()).build();

    }
}

