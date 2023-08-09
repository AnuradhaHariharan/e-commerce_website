package com.example.amazoff.Service;

import com.example.amazoff.Dto.Request.OrderRequestDto;
import com.example.amazoff.Dto.Response.OrderResponseDto;
import com.example.amazoff.Enum.ProductStatus;
import com.example.amazoff.Exception.CustomerNotFound;
import com.example.amazoff.Exception.InsufficientQuantityException;
import com.example.amazoff.Exception.InvalidCardException;
import com.example.amazoff.Exception.ProductNotFound;
import com.example.amazoff.Model.*;
import com.example.amazoff.Repository.*;
import com.example.amazoff.Transformers.ItemTransformer;
import com.example.amazoff.Transformers.OrderTransformer;
import com.example.amazoff.Transformers.ProductTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    JavaMailSender javaMailSender;
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) {
        Customer customer=customerRepository.getByEmailId(orderRequestDto.getCustomerEmail());
        if(customer==null){
            throw new CustomerNotFound("Customer doesn't exists.");
        }
        Optional<Product> optionalProduct=productRepository.findById(orderRequestDto.getProductId());
        if(!optionalProduct.isPresent()){
            throw new ProductNotFound("Product doesn't exists.");
        }
        Product product=optionalProduct.get();
      Card card=cardRepository.findByCardNo(orderRequestDto.getCardUsed());

        if(card==null ){
            throw new InvalidCardException("Invalid card.");
        }
        if(card.getCvv()!=orderRequestDto.getCvv()){
            throw new InvalidCardException("Incorrect CVV Number");
        }
        Date validTill=card.getValidTill();
        Date todaysDate=new Date();
        if(validTill.before(todaysDate)){
            throw new InvalidCardException("Card Expired");
        }
        if(product.getAvailableQuantity()< orderRequestDto.getRequiredQuantity()){
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
        sendMail(savedOrder);
        return OrderTransformer.entityToResponseDto(savedOrder);
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
        sendMail(orderEntity);
        return orderEntity;
    }
    public void sendMail(OrderEntity orderEntity){
        String itemListText = generateItemListText(orderEntity.getItemList());

        String text = "Dear " + orderEntity.getCustomer().getName() + ",\n" +
                "Thank you for shopping with us! We are thrilled to confirm your recent order.\n" +
                "Your satisfaction is our top priority, and we are here to make sure you have a wonderful shopping experience.\n" +
                "Order details:\n" +
                "Order Id: " + orderEntity.getOrderId() + "\n" +
                "Order total: " + orderEntity.getOrderTotal() + "\n" +
                "Order date: " + orderEntity.getDate() + "\n" +
                itemListText;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(orderEntity.getCustomer().getEmailId());
        simpleMailMessage.setFrom("springbootprojectjava@gmail.com");
        simpleMailMessage.setSubject("Order Confirmation");
        simpleMailMessage.setText(text);
        javaMailSender.send(simpleMailMessage);
    }

    private String generateItemListText(List<Item> itemList) {
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
}
