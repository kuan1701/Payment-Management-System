package com.webproject.pms.service;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;

import java.util.List;

public interface BankCardService {

	Boolean save(BankCard bankCard);
	
	Boolean addNewBankCard(BankCard bankCard, Account account);
	
	Boolean blockCard(Long cardId);
	
	Boolean unblockCard(Long cardId);
	
	void deleteCard(BankCard card);
	
	BankCard findCardByCardId(Long cardId);
	
	List<BankCard> findCardsByAccountId(Long accountId);
}
