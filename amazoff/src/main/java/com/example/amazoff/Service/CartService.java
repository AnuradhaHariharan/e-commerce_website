package com.example.amazoff.Service;

import com.example.amazoff.Dto.Request.CheckOutCartRequestDto;
import com.example.amazoff.Dto.Request.ItemRequestDto;
import com.example.amazoff.Dto.Response.CartResponseDto;
import com.example.amazoff.Dto.Response.ItemResponseDto;
import com.example.amazoff.Dto.Response.OrderResponseDto;
import com.example.amazoff.Exception.CustomerNotFound;
import com.example.amazoff.Exception.EmptyCartException;
import com.example.amazoff.Exception.InvalidCardException;
import com.example.amazoff.Model.*;
import com.example.amazoff.Repository.*;
import com.example.amazoff.Transformers.CartTransformer;
import com.example.amazoff.Transformers.OrderTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class CartService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderEntityRepository orderEntityRepository;
    public CartResponseDto addItemToCart(ItemRequestDto itemRequestDto, Item item) {
        Customer customer=customerRepository.getByEmailId(itemRequestDto.getCustomerEmail());
        Product product=item.getProduct();
        Cart cart=customer.getCart();
        cart.setCartTotal(cart.getCartTotal()+ product.getProductPrice()* itemRequestDto.getRequiredQuantity());
        item.setCart(cart);
        Item savedItem= itemRepository.save(item);
        cart.getItemList().add(savedItem);
        product.getItemList().add(savedItem);

       Cart savedCart= cartRepository.save(cart);
        productRepository.save(product);
        return CartTransformer.entityToResponseDto(savedCart);

    }

    public OrderResponseDto checkOutCart(CheckOutCartRequestDto checkOutCartRequestDto) {
        Customer customer=customerRepository.getByEmailId(checkOutCartRequestDto.getEmailId());
        if(customer==null){
            throw new CustomerNotFound("Customer doesn't exist.");
        }
        Card card=cardRepository.findByCardNo(checkOutCartRequestDto.getCardNo());
        if(card==null){
            throw new InvalidCardException("Invalid Card.");
        }
        if(card.getCvv()!= checkOutCartRequestDto.getCvv()){
            throw new InvalidCardException("Invalid CVV.");
        }
        Date date=new Date();
        if(card.getValidTill().before(date)){
            throw new InvalidCardException("Card Expired.");
        }
        Cart cart=customer.getCart();
        if(cart.getItemList().size()==0){
            throw new EmptyCartException("Sorry! The cart is empty.");
        }
        OrderEntity orderEntity=orderService.placeOrder(card,cart);
        resetCart(cart);
        OrderEntity savedOrder=orderEntityRepository.save(orderEntity);
        return OrderTransformer.entityToResponseDto(savedOrder);
    }

    private void resetCart(Cart cart) {
        cart.setCartTotal(0);
        for(Item item:cart.getItemList()){
            item.setCart(null);
        }
        cart.setItemList(new ArrayList<>());

    }
}
