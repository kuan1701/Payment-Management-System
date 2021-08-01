package com.webproject.pms.service;

import com.webproject.pms.model.entities.Letter;

import java.security.Principal;
import java.util.List;

public interface LetterService {
	
	Boolean addNewLetter(Letter letter, Principal principal);
	
	Boolean updateLetterByLetterId(Long letterId);
	
	Boolean deleteLetterByLetterId(Long letterId);
	
	Letter findLetterByLetterId(Long letterId);
	
	List<Letter> findLettersByUserId(Long userId);
	
	List<Letter> findUnprocessedLetters();
	
	List<Letter> findAllLetters();
	
	List<Letter> searchByCriteria(String typeQuestion, String startDate, String finalDate);
	
	List<Letter> searchByCriteria(String startDate, String finalDate);
}
