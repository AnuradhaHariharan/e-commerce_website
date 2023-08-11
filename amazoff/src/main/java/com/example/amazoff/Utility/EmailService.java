package com.example.amazoff.Utility;

import com.example.amazoff.Model.Item;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Handle messaging exception
            e.printStackTrace();
        }
    }
    public void sendEmailWithAttachment(String to, String subject, String content, List<Item> orderedItems) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            // Create the document content
            String documentContent = generateDocumentContent(orderedItems);
            byte[] documentBytes = documentContent.getBytes();

            // Attach the document
            ByteArrayDataSource dataSource = new ByteArrayDataSource(documentBytes, "application/octet-stream");
            helper.addAttachment("OrderedItems.txt", dataSource);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Handle messaging or IO exception
            e.printStackTrace();
        }
    }

    private String generateDocumentContent(List<Item> orderedItems) {
        // Generate the content of the document based on the ordered items
        StringBuilder documentContent = new StringBuilder("Ordered Items:\n");
        for (Item item : orderedItems) {
            String itemContent = "Product: " + item.getProduct().getProductName() +
                    ", Quantity: " + item.getRequiredQuantity() +
                    ", Price: " + item.getProduct().getProductPrice() +
                    "\n";
            documentContent.append(itemContent);
        }
        return documentContent.toString();
    }
}

