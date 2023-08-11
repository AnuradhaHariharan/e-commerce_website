package com.example.amazoff.Service;

import com.example.amazoff.Dto.Request.OrderRequestDto;
import com.example.amazoff.Dto.Response.OrderResponseDto;
import com.example.amazoff.Enum.ProductStatus;
import com.example.amazoff.Exception.*;
import com.example.amazoff.Model.*;
import com.example.amazoff.Repository.*;
import com.example.amazoff.Transformers.ItemTransformer;
import com.example.amazoff.Transformers.OrderTransformer;
import com.example.amazoff.Transformers.ProductTransformer;
import com.example.amazoff.Utility.EmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    CardService cardService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderEntityRepository orderEntityRepository;
    @Autowired
    EmailService emailService;
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) {
        Customer customer=customerRepository.getByEmailId(orderRequestDto.getCustomerEmail());
        if(customer==null){
            throw new CustomerNotFound("Customer doesn't exists.");
        }
        if(!customer.getPassword().equals(orderRequestDto.getPassword())){
            throw new IncorrectPasswordException("Incorrect password.");
        }
        Optional<Product> optionalProduct=productRepository.findById(orderRequestDto.getProductId());
        if(!optionalProduct.isPresent()){
            sendOrderFailedEmail(orderRequestDto,"Product doesn't exists");
            throw new ProductNotFound("Product doesn't exists.");
        }

        Product product=optionalProduct.get();
      Card card=cardRepository.findByCardNo(orderRequestDto.getCardUsed());

        if(card==null ){
            sendOrderFailedEmail(orderRequestDto,"Invalid card");
            throw new InvalidCardException("Invalid card.");
        }
        if(card.getCvv()!=orderRequestDto.getCvv()){
            sendOrderFailedEmail(orderRequestDto,"Incorrect cvv number entered");
            throw new InvalidCardException("Incorrect CVV Number");
        }
        Date validTill=card.getValidTill();
        Date todaysDate=new Date();
        if(validTill.before(todaysDate)){
            sendOrderFailedEmail(orderRequestDto,"card expired");
            throw new InvalidCardException("Card Expired");
        }
        if(product.getAvailableQuantity()< orderRequestDto.getRequiredQuantity()){
            sendOrderFailedEmail(orderRequestDto,"insufficient quantity");
            throw new InsufficientQuantityException("Insufficient Quantity");
        }
        int newQuantity= product.getAvailableQuantity()-orderRequestDto.getRequiredQuantity();
        product.setAvailableQuantity(newQuantity);
        if(newQuantity==0){
            product.setProductStatus(ProductStatus.OUT_OF_STOCK);
        }

        OrderEntity orderEntity=new OrderEntity();
        orderEntity.setOrderTotal(orderRequestDto.getRequiredQuantity()*product.getProductPrice());
        orderEntity.setOrderId(String.valueOf(UUID.randomUUID()));
        orderEntity.setCustomer(customer);
        orderEntity.setCardUsed(cardService.generateMaskedCard(orderRequestDto.getCardUsed()));

        Item item = Item.builder()
                .product(product) // Set the Product relationship
                .orderEntity(orderEntity) // Set the OrderEntity relationship
                .requiredQuantity(orderRequestDto.getRequiredQuantity())
                .cart(null)// Set other properties of Item
                .build();


        orderEntity.getItemList().add(item);

        OrderEntity savedOrder=orderEntityRepository.save(orderEntity);
       // savedOrder.setCustomer(customer);
        product.getItemList().add(savedOrder.getItemList().get(0));
        customer.getOrder().add(savedOrder);
       // customerRepository.save(customer);
        //productRepository.save(product);
        sendOrderConfirmationEmail(orderEntity);
        return OrderTransformer.entityToResponseDto(savedOrder);
    }
    public List<OrderResponseDto> getAllOrdersOfCustomer(String emailId, String password) {
        Customer customer=customerRepository.getByEmailId(emailId);
        if(customer==null){
            throw new CustomerNotFound("Account not found");
        }
        if(!customer.getPassword().equals(password)){
            throw new IncorrectPasswordException("Incorrect password entered.");
        }
        List<OrderEntity> orderList = orderEntityRepository.findByCustomer(customer);
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();

        for (OrderEntity orderEntity : orderList) {
            OrderResponseDto orderResponseDto = OrderTransformer.entityToResponseDto(orderEntity);
            orderResponseDtoList.add(orderResponseDto);
        }

        return orderResponseDtoList;

    }

    public OrderEntity placeOrder(Card card, Cart cart) {
        OrderEntity orderEntity=new OrderEntity();
        orderEntity.setOrderId(String.valueOf(UUID.randomUUID()));
        orderEntity.setCardUsed(cardService.generateMaskedCard(card.getCardNo()));
        int orderTotal=0;
        for(Item item: cart.getItemList()){
            Product product=item.getProduct();
            if(product.getAvailableQuantity()< item.getRequiredQuantity()){
                throw new InsufficientQuantityException("Sorry!Insufficient Quantity Available for: "+product.getProductName());
            }
            int newQuantity= product.getAvailableQuantity()- item.getRequiredQuantity();
            product.setAvailableQuantity(newQuantity);
            if(newQuantity==0){
                product.setProductStatus(ProductStatus.OUT_OF_STOCK);
            }
            orderTotal+=product.getProductPrice()*item.getRequiredQuantity();
            item.setOrderEntity(orderEntity);
        }
        orderEntity.setOrderTotal(orderTotal);
        orderEntity.setItemList(cart.getItemList());
        orderEntity.setCustomer(card.getCustomer());
   sendOrderConfirmationEmail(orderEntity);
        return orderEntity;
    }
    private void sendOrderConfirmationEmail(OrderEntity orderEntity) {
        String itemListText =generateItemListText(orderEntity.getItemList());

        String text = "Dear " + orderEntity.getCustomer().getName() + ",\n" +
                "Thank you for shopping with us! We are thrilled to confirm your recent order.\n" +
                "Your satisfaction is our top priority, and we are here to make sure you have a wonderful shopping experience.\n" +
                "Order details:\n" +
                "Order Id: " + orderEntity.getOrderId() + "\n" +
                "Order total: " + orderEntity.getOrderTotal() + "\n" +
                "Order date: " + orderEntity.getDate() + "\n" +
                itemListText;

        emailService.sendEmailWithAttachment(orderEntity.getCustomer().getEmailId(), "Order Confirmation", text, orderEntity.getItemList());
    }
    public String generateItemListText(List<Item> itemList) {
        StringBuilder itemListText = new StringBuilder("Order items:\n");
        for (Item item : itemList) {
            String itemText = "Item: " + item.getProduct().getProductName() +
                    ", Quantity: " + item.getRequiredQuantity() +
                    ", Price: " + item.getProduct().getProductPrice() +
                    "\n";
            itemListText.append(itemText);
        }
        return itemListText.toString();
    }
    public void sendOrderFailedEmail(OrderRequestDto orderRequestDto, String errorMessage) {
        String text = "Dear " + orderRequestDto.getCustomerEmail() + ",\n" +
                "We regret to inform you that your recent order has failed due to the following reason:\n" +
                errorMessage +
                "\nPlease try again or contact our support for assistance.";

        emailService.sendEmail(orderRequestDto.getCustomerEmail(), "Order Failed", text);
    }


}






