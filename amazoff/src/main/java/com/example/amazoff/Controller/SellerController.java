package com.example.amazoff.Controller;

import com.example.amazoff.Dto.Request.SellerRequestDto;
import com.example.amazoff.Dto.SellerResponseDto;
import com.example.amazoff.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    SellerService sellerService;

     @PostMapping("/add_seller")
    public ResponseEntity addSeller(@RequestBody SellerRequestDto sellerRequestDto){
        SellerResponseDto sellerResponseDto=sellerService.addSeller(sellerRequestDto);
        return new ResponseEntity(sellerResponseDto, HttpStatus.ACCEPTED);
    }
    @PutMapping("/forgot_password")
    public ResponseEntity forgotPassword(@RequestParam("email") String email) {
        try {
            // Generate and send OTP to the customer's email
            sellerService.sendPasswordResetOtp(email);
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
            sellerService.resetPasswordWithOtp(email, otp, newPassword);
            return new ResponseEntity("Password reset successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
