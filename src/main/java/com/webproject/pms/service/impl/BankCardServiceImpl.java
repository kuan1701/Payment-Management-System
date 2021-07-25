package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.dao.BankCardDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.service.BankCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BankCardServiceImpl implements BankCardService {
	
	private static final Logger LOGGER = LogManager.getLogger(BankCardService.class);
	private final BankCardDao bankCardDao;
	private final PasswordEncoder passwordEncoder;
	private final MapStructMapper mapStructMapper;
	
	@Autowired
	public BankCardServiceImpl(BankCardDao bankCardDao, AccountDao accountDao, PasswordEncoder passwordEncoder, MapStructMapper mapStructMapper) {
		
		this.bankCardDao = bankCardDao;
		this.passwordEncoder = passwordEncoder;
		this.mapStructMapper = mapStructMapper;
	}
	
	@Override
	public BankCard save(BankCard bankCard) {
		
		return bankCardDao.save(bankCard);
	}
	
	@Override
	public Boolean addNewBankCard(BankCard bankCard, Account account) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/yy");
		Date date = null;
		
		try {
			date = formatter.parse(bankCard.getMonth() + "/" + bankCard.getYear());
		} catch (ParseException e) {
			LOGGER.error("ParseException: " + e.getMessage());
		}
		
		if (bankCardDao.findBankCardByNumber(bankCard.getNumber()) != null
				|| account == null
		) {
			return false;
		}
		bankCard.setAccount(account);
		bankCard.setActive(true);
		bankCard.setValidity(formatter.format(date));
		bankCardDao.save(bankCard);
		return true;
	}
	
	@Override
	public Boolean blockCard(Long cardId) {
	
		if (bankCardDao.findById(cardId).isPresent()) {
			BankCard bankCard = bankCardDao.getById(cardId);
			bankCard.setActive(false);
			bankCardDao.save(bankCard);
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean unblockCard(Long cardId) {
		
		if (bankCardDao.findById(cardId).isPresent()) {
			BankCard bankCard = bankCardDao.getById(cardId);
			bankCard.setActive(true);
			bankCardDao.save(bankCard);
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean deleteCardByCardId(Long cardId) {
		
		if (bankCardDao.existsById(cardId)) {
			bankCardDao.deleteById(cardId);
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean deleteCardByCardNumber(String cardNumber) {
		
		if (bankCardDao.findBankCardByNumber(cardNumber) != null) {
			bankCardDao.deleteBankCardByNumber(cardNumber);
			return true;
		}
		return false;
	}
	
	@Override
	public void deleteCard(BankCard card) {
		bankCardDao.delete(card);
	}
	
	@Override
	public BankCard findCardByCardId(Long cardId) {
		
		return bankCardDao.getById(cardId);
	}
	
	@Override
	public BankCard findCardByCardNumber(String number) {
		
		return bankCardDao.findBankCardByNumber(number);
	}
	
	@Override
	public List<BankCard> findCardsByAccountId(Long accountId) {
		
		return bankCardDao.findBankCardsByAccount_AccountId(accountId);
	}
	
	@Override
	public List<BankCard> findAllCards() {
		
		return bankCardDao.findAll();
	}
}
