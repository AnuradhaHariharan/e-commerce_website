package com.example.amazoff.Controller;

import com.example.amazoff.Dto.CustomerResponseDto;
import com.example.amazoff.Dto.Request.CustomerRequestDto;
import com.example.amazoff.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
  @PostMapping("/add_customer")
    ResponseEntity addCustomer(@RequestBody CustomerRequestDto customerRequestDto){
        try{
        CustomerResponseDto customerResponseDto=customerService.addCustomer(customerRequestDto);
        return new ResponseEntity(customerResponseDto, HttpStatus.ACCEPTED);
    }catch(Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
