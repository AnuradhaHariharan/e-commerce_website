package com.example.amazoff.Controller;

import com.example.amazoff.Dto.Request.CheckOutCartRequestDto;
import com.example.amazoff.Dto.Request.ItemRequestDto;
import com.example.amazoff.Dto.Response.CartResponseDto;
import com.example.amazoff.Dto.Response.OrderResponseDto;
import com.example.amazoff.Model.Item;
import com.example.amazoff.Service.CartService;
import com.example.amazoff.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    ItemService itemService;

    @Autowired
    CartService cartService;
    @PostMapping("/add")
    public ResponseEntity addToCart(@RequestBody ItemRequestDto itemRequestDto){
        try{
        Item item=itemService.createItem(itemRequestDto);
        CartResponseDto cartResponseDto=cartService.addItemToCart(itemRequestDto,item);
        return new ResponseEntity(cartResponseDto,HttpStatus.CREATED);
    }catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/checkout")
    public ResponseEntity checkOutCart(@RequestBody CheckOutCartRequestDto checkOutCartRequestDto){
        try{
        OrderResponseDto orderResponseDto=cartService.checkOutCart(checkOutCartRequestDto);
        return new ResponseEntity(orderResponseDto,HttpStatus.ACCEPTED);
    }catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
}
