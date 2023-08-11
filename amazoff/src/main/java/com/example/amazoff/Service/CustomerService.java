package com.example.amazoff.Service;

import com.example.amazoff.Dto.CustomerResponseDto;
import com.example.amazoff.Dto.Request.CustomerRequestDto;
import com.example.amazoff.Exception.CustomerNotFound;
import com.example.amazoff.Exception.IncorrectOtpException;
import com.example.amazoff.Model.Cart;
import com.example.amazoff.Model.Customer;
import com.example.amazoff.Repository.CustomerRepository;
import com.example.amazoff.Transformers.CustomerTransformer;
import com.example.amazoff.Utility.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service

public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmailService emailService;

    public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto) {
        Customer customer =CustomerTransformer.requestDtoToCustomer(customerRequestDto);
        customer.setResetOtp(null);
        customer.setResetOtpExpiry(null);

        Cart cart = Cart.builder().cartTotal(0).customer(customer).build();
        customer.setCart(cart);

        customerRepository.save(customer);

        CustomerResponseDto customerResponseDto= CustomerTransformer.customerToCustomerResponseDto(customer);
        sendWelcomeEmail(customer.getEmailId());
        return customerResponseDto;
    }
    private void sendWelcomeEmail(String userEmail) {
        String subject = "Welcome to Shopping Verse";
        String content = "Dear user,\n\n" +
                "Welcome to Shopping Verse! Your account has been successfully created.\n" +
                "Start exploring our platform and enjoy the shopping experience.\n\n" +
                "Best regards,\n" +
                "The Shopping Verse Team";

        emailService.sendEmail(userEmail, subject, content);
    }

    public void sendPasswordResetOtp(String email) {
        Customer customer=customerRepository.getByEmailId(email);
        if(customer==null){
            throw new CustomerNotFound("Account not found.Please check your Mail id.");
        }
        String otp = generateOTP(); // Generate OTP
        customer.setResetOtp(otp);
        customer.setResetOtpExpiry(LocalDateTime.now().plusMinutes(15)); // OTP valid for 15 minutes
        customerRepository.save(customer);
        // Send OTP to customer's email
        String subject = "Password Reset OTP";
        String content = "Dear " + customer.getName() + ",<br><br>" +
                "You have requested to reset your password for your ShoppingVerse account. Please use the following OTP to complete the password reset process:<br><br>" +
                "<strong>OTP: " + otp + ".</strong><br><br>" +
                "If you did not initiate this request, please ignore this email.<br><br>" +
                "Thank you,<br>" +
                "The ShoppingVerse Team";
        emailService.sendEmail(customer.getEmailId(), subject, content);
    }
    public void resetPasswordWithOtp(String email, String otp, String newPassword) {
        Customer customer = customerRepository.getByEmailId(email);
        if (customer == null) {
            throw new CustomerNotFound("Customer not found");
        }

        String storedOtp = customer.getResetOtp();
        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new IncorrectOtpException("Incorrect OTP");
        }

        // Update the customer's password
        customer.setPassword(newPassword);
        customer.setResetOtp(null); // Clear OTP
        customerRepository.save(customer);
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
}


