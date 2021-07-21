package com.webproject.pms.service;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;

import java.util.List;

public interface BankCardService {
	
	BankCard save(BankCard bankCard);
	
	Boolean addNewBankCard(BankCard bankCard, Account account);
	
	Boolean blockCard(Long cardId);
	
	Boolean unblockCard(Long cardId);
	
	Boolean deleteCardByCardId(Long cardId);
	
	Boolean deleteCardByCardNumber(String cardNumber);
	
	BankCard findCardByCardId(Long cardId);
	
	BankCard findCardByCardNumber(String number);
	
	List<BankCard> findCardsByAccountId(Long accountId);
	
	List<BankCard> findAllCards();
}
