package com.example.amazoff.Controller;

import com.example.amazoff.Dto.Request.OrderRequestDto;
import com.example.amazoff.Dto.Response.OrderResponseDto;
import com.example.amazoff.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
@PostMapping("/placeorder")
    public ResponseEntity placeOrder(@RequestBody OrderRequestDto orderRequestDto){
        try{
        OrderResponseDto orderResponseDto=orderService.placeOrder(orderRequestDto);
        return new ResponseEntity(orderResponseDto, HttpStatus.CREATED);
    }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
