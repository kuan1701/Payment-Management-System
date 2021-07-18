package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.BankCardDao;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.service.BankCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
	private final MapStructMapper mapStructMapper;
	
	@Autowired
	public BankCardServiceImpl(BankCardDao bankCardDao, MapStructMapper mapStructMapper) {
		
		this.bankCardDao = bankCardDao;
		this.mapStructMapper = mapStructMapper;
	}
	
	@Override
	public BankCard save(BankCard bankCard) {
		
		return bankCardDao.save(bankCard);
	}
	
	@Override
	public Boolean addNewBankCard(BankCard bankCard, String month, String year) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
		Date date = null;
		
		try {
			date = formatter.parse(month + "/" + year);
		} catch (ParseException e) {
			LOGGER.error("ParseException: " + e.getMessage());
		}
		
		if (bankCardDao.findBankCardByNumber(bankCard.getNumber()) == null) {
			bankCard.setValidity(formatter.format(date));
			bankCard.setActive(true);
			bankCardDao.save(bankCard);
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean blockCard(Long cardId) {
		
		if (cardId != null) {
			BankCard bankCard = bankCardDao.getOne(cardId);
			bankCard.setActive(false);
			bankCardDao.save(bankCard);
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean unblockCard(Long cardId) {
		
		if (cardId != null) {
			BankCard bankCard = bankCardDao.getOne(cardId);
			bankCard.setActive(false);
			bankCardDao.save(bankCard);
			return false;
		}
		return true;
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
