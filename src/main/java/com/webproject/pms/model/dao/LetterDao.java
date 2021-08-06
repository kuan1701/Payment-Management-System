package com.webproject.pms.model.dao;

import com.webproject.pms.model.entities.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LetterDao extends JpaRepository<Letter, Long> {

	@Query(value = "SELECT * FROM action_log WHERE user_id = ? ORDER BY date DESC",
	nativeQuery = true)
	List<Letter> findLettersByUser_UserId(Long userId);
	
	@Query(value = "SELECT * FROM letter"
			+ " WHERE is_processed = 0 AND type_question LIKE ? AND date BETWEEN"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s') AND"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s)') ORDER BY date;",
			nativeQuery = true)
	List<Letter> searchByCriteria(@Param("typeQuestion") String typeQuestion,
	                              @Param("startDate") String startDate,
	                              @Param("finalDate") String finalDate);
	
	@Query(value = "SELECT * FROM letter"
			+ " WHERE is_processed = 0 AND date BETWEEN"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s') AND"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s)') ORDER BY date;",
			nativeQuery = true)
	List<Letter> searchByCriteria(@Param("startDate") String startDate,
	                              @Param("finalDate") String finalDate);
}
