package com.example.amazoff.Service;

import com.example.amazoff.Dto.Request.SellerRequestDto;
import com.example.amazoff.Dto.SellerResponseDto;
import com.example.amazoff.Exception.CustomerNotFound;
import com.example.amazoff.Exception.IncorrectOtpException;
import com.example.amazoff.Exception.InvalidPanIdException;
import com.example.amazoff.Exception.SellerNotFound;
import com.example.amazoff.Model.Customer;
import com.example.amazoff.Model.Seller;
import com.example.amazoff.Repository.SellerRepository;
import com.example.amazoff.Transformers.SellerTransformer;
import com.example.amazoff.Utility.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SellerService {
@Autowired
    SellerRepository sellerRepository;
@Autowired
    EmailService emailService;
    public SellerResponseDto addSeller(SellerRequestDto sellerRequestDto) {
        if (!isValidPanId(sellerRequestDto.getPanId())) {
            throw new InvalidPanIdException("Invalid PAN ID format.");
        }
        Seller seller= SellerTransformer.requestDtoToEntity(sellerRequestDto);
        sellerRepository.save(seller);

        return SellerTransformer.EntityToResponseDto(seller);
    }
    private boolean isValidPanId(String panId) {
        // Define the PAN ID regex pattern
        String panIdPattern = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(panIdPattern);

        // Match the input PAN ID against the pattern
        Matcher matcher = pattern.matcher(panId);

        // Return true if the PAN ID matches the pattern
        return matcher.matches();
    }

    public void sendPasswordResetOtp(String email) {
        Seller seller=sellerRepository.getByEmailId(email);
        if(seller==null){
            throw new CustomerNotFound("Account not found.Please check your Mail id.");
        }
        String otp = generateOTP(); // Generate OTP
        seller.setResetOtp(otp);
        seller.setResetOtpExpiry(LocalDateTime.now().plusMinutes(15)); // OTP valid for 15 minutes
        sellerRepository.save(seller);
        // Send OTP to customer's email
        String subject = "Password Reset OTP";
        String content = "Dear " + seller.getName() + ",<br><br>" +
                "You have requested to reset your password for your ShoppingVerse seller account. Please use the following OTP to complete the password reset process:<br><br>" +
                "<strong>OTP: " + otp + ".</strong><br><br>" +
                "If you did not initiate this request, please ignore this email.<br><br>" +
                "Thank you,<br>" +
                "The ShoppingVerse Team";
        emailService.sendEmail(seller.getEmailId(), subject, content);
    }

    private String generateOTP() {
        int otpLength = 6; // You can adjust the OTP length as needed
        String allowedChars = "0123456789";
        StringBuilder otp = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < otpLength; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            otp.append(allowedChars.charAt(randomIndex));
        }

        return otp.toString();
    }

    public void resetPasswordWithOtp(String email, String otp, String newPassword) {
        Seller seller = sellerRepository.getByEmailId(email);
        if (seller == null) {
            throw new SellerNotFound("Account not found");
        }

        String storedOtp = seller.getResetOtp();
        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new IncorrectOtpException("Incorrect OTP");
        }

        // Update the customer's password
        seller.setPassword(newPassword);
        seller.setResetOtp(null); // Clear OTP
        sellerRepository.save(seller);
    }
}








