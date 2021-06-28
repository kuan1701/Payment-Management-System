package com.webproject.pms.model.dao;

import com.webproject.pms.model.entities.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankCardDao extends JpaRepository<BankCard, Long> {
	
	Boolean deleteBankCardByNumber(String cardNumber);
	
	BankCard findBankCardByNumber(String number);
	
	List<BankCard> findBankCardsByAccount_AccountId(Long accountId);
}
