package com.webproject.pms.model.dao;

import com.webproject.pms.model.entities.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionLogDao extends JpaRepository<LogEntry, Long> {
	
	@Query(value = "DELETE FROM log_entry WHERE user_user_id = ?", nativeQuery = true)
	void deleteAllByUserId(Long userId);
	
	List<LogEntry> findLogEntriesByUser_UserId(Long userId);
	
	@Query(
			value = "SELECT * FROM log_entry"
					+ " WHERE user_user_id = ? AND date BETWEEN"
					+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s') AND"
					+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s)') ORDER BY date DESC;",
			nativeQuery = true)
	List<LogEntry> searchByCriteria(Long userId, String startDate, String finalDate);
}
