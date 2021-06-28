package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.LetterDao;
import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.service.LetterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class LetterServiceImpl implements LetterService {
	
	private static final Logger LOGGER = LogManager.getLogger(LetterServiceImpl.class);
	private final LetterDao letterDao;
	
	@Autowired
	public LetterServiceImpl(LetterDao letterDao) {
		this.letterDao = letterDao;
	}
	
	@Override
	@Transactional
	public Letter addNewLetter(Letter letter) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		letter.setDate(formatter.format(new Date()));
		letter.setProcessed(false);
		
		return letterDao.save(letter);
	}
	
	@Override
	@Transactional
	public Boolean updateLetterByLetterId(Long letterId) {
		
		if (letterId != null) {
			Letter letter = findLetterByLetterId(letterId);
			letter.setProcessed(true);
			letterDao.save(letter);
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional
	public Boolean deleteLetterByLetterId(Long letterId) {
		
		if (letterDao.existsById(letterId)) {
			letterDao.deleteById(letterId);
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional
	public Letter findLetterByLetterId(Long letterId) {
		return letterDao.getOne(letterId);
	}
	
	@Override
	@Transactional
	public List<Letter> findLettersByUserId(Long userId) {
		
		return letterDao.findLettersByUser_UserId(userId);
	}
	
	@Override
	@Transactional
	public List<Letter> findUnprocessedLetters() {
		
		List<Letter> letters = findAllLetters();
		List<Letter> unprocessedLetters = new ArrayList<>();
		for (Letter letter : letters) {
			if (!letter.getProcessed()) {
				unprocessedLetters.add(letter);
			}
		}
		return unprocessedLetters;
	}
	
	@Override
	@Transactional
	public List<Letter> findAllLetters() {
		return letterDao.findAll();
	}
	
	@Override
	@Transactional
	public List<Letter> searchByCriteria(String typeQuestion, String startDate, String finalDate) {
		return letterDao.searchByCriteria(typeQuestion, startDate, finalDate);
	}
	
	@Override
	@Transactional
	public List<Letter> searchByCriteria(String startDate, String finalDate) {
		return letterDao.searchByCriteria(startDate, finalDate);
	}
}
