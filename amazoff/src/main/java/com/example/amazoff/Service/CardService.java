package com.example.amazoff.Service;

import com.example.amazoff.Dto.Request.CardRequestDto;
import com.example.amazoff.Dto.Response.CardResponseDto;
import com.example.amazoff.Exception.CustomerNotFound;
import com.example.amazoff.Exception.IncorrectPasswordException;
import com.example.amazoff.Exception.InvalidCardException;
import com.example.amazoff.Model.Card;
import com.example.amazoff.Model.Customer;
import com.example.amazoff.Repository.CardRepository;
import com.example.amazoff.Repository.CustomerRepository;
import com.example.amazoff.Transformers.CardTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class CardService {
    private static final String CARD_NUMBER_REGEX = "^(\\d{16})$";
    @Autowired
    CustomerRepository customerRepository;

    public CardResponseDto addCard(CardRequestDto cardRequestDto) {
        Customer customer=customerRepository.getByEmailId(cardRequestDto.getCustomerEmail());
        if(customer==null){
            throw new CustomerNotFound("Person Not Found");
        }
        if(customer.getPassword()!=cardRequestDto.getPassword()){
            throw new IncorrectPasswordException("Incorrect password.");
        }
        String cardNo = cardRequestDto.getCardNo();
        if (!isValidCardNumber(cardNo)) {
            throw new InvalidCardException("Invalid card number format");
        }

        Card card= CardTransformer.requestDtoToEntity(cardRequestDto);
        card.setCustomer(customer);
        customer.getCards().add(card);
        Customer savedCustomer=customerRepository.save(customer);
        List<Card> cardList=customer.getCards();
        Card latestCard=cardList.get(cardList.size()-1);
        CardResponseDto cardResponseDto=CardTransformer.entityToResponseDto(card);
        cardResponseDto.setCardNo(generateMaskedCard(latestCard.getCardNo()));
   return cardResponseDto;
    }

    private boolean isValidCardNumber(String cardNo) {
        return Pattern.matches(CARD_NUMBER_REGEX, cardNo);
    }

    public String generateMaskedCard(String cardNo){
        int cardLength=cardNo.length();
        String maskedCardNo="";
        for(int i=0;i<cardLength-4;i++){
            maskedCardNo+="X";
        }
        maskedCardNo+=cardNo.substring(cardLength-4);
        return maskedCardNo;
    }
}
