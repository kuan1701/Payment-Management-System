package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.LetterDao;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.LetterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@Transactional
public class LetterServiceImpl implements LetterService {
	
	private static final Logger LOGGER = LogManager.getLogger(LetterServiceImpl.class);
	private final LetterDao letterDao;
	private final UserDao userDao;
	
	@Autowired
	public LetterServiceImpl(LetterDao letterDao, UserDao userDao) {
		this.letterDao = letterDao;
		this.userDao = userDao;
	}
	
	@Override
	public Boolean addNewLetter(Letter letter, Principal principal) {
		
		User user = userDao.findUserByUsername(principal.getName());
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		letter.setDate(formatter.format(new Date()));
		letter.setUser(user);
		letter.setProcessed(false);
		letterDao.save(letter);
		return true;
	}
	
	@Override
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
	public Boolean deleteLetterByLetterId(Long letterId) {
		
		if (letterDao.existsById(letterId)) {
			letterDao.deleteById(letterId);
			return true;
		}
		return false;
	}
	
	@Override
	public Letter findLetterByLetterId(Long letterId) {
		return letterDao.getById(letterId);
	}
	
	@Override
	public List<Letter> findLettersByUserId(Long userId) {
		return letterDao.findLettersByUser_UserId(userId);
	}
	
	@Override
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
	public List<Letter> findAllLetters() {
		return letterDao.findAll();
	}
	
	@Override
	public List<Letter> searchByCriteria(String typeQuestion, String startDate, String finalDate) {
		return letterDao.searchByCriteria(typeQuestion, startDate, finalDate);
	}
	
	@Override
	public List<Letter> searchByCriteria(String startDate, String finalDate) {
		return letterDao.searchByCriteria(startDate, finalDate);
	}
}
