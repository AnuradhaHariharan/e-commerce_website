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
    @PutMapping("/forgot_password")
   public ResponseEntity forgotPassword(@RequestParam("email") String email) {
        try {
            // Generate and send OTP to the customer's email
            customerService.sendPasswordResetOtp(email);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/reset_password")
    public ResponseEntity resetPasswordWithOtp(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        try {
            customerService.resetPasswordWithOtp(email, otp, newPassword);
            return new ResponseEntity("Password reset successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
