package com.webproject.pms.service;

import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.dto.BankCardGetDto;

import java.util.List;

public interface BankCardService {
	
	BankCard save(BankCard bankCard);
	
	Boolean addNewBankCard(BankCard bankCard, String month, String year);
	
	Boolean blockCard(Long cardId);
	
	Boolean unblockCard(Long cardId);
	
	Boolean deleteCardByCardId(Long cardId);
	
	Boolean deleteCardByCardNumber(String cardNumber);
	
	BankCardGetDto findCardByCardId(Long cardId);
	
	BankCardGetDto findCardByCardNumber(String number);
	
	List<BankCardGetDto> findCardsByAccountId(Long accountId);
	
	List<BankCardGetDto> findAllCards();
}
