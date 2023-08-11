package com.example.amazoff.Controller;

import com.example.amazoff.Dto.Request.OrderRequestDto;
import com.example.amazoff.Dto.Response.OrderResponseDto;
import com.example.amazoff.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/getallordersofcustomer")
    public ResponseEntity getAllOrdersOfCustomer(@RequestParam("email")String emailId,@RequestParam("password")String password){
         try{
    List<OrderResponseDto> orderResponseDtoList=orderService.getAllOrdersOfCustomer(emailId,password);
    return new ResponseEntity(orderResponseDtoList,HttpStatus.ACCEPTED);
    }catch(Exception e){
             return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
         }
    }
}
