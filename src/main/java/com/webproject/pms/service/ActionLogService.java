package com.webproject.pms.service;

import com.webproject.pms.model.entities.LogEntry;

import java.util.List;

public interface ActionLogService {
	
	LogEntry save(LogEntry logEntry);
	
	Boolean clearActionLog(Long userId);
	
	LogEntry findLogEntryByLogEntryId(Long logEntryId);
	
	List<LogEntry> findLogEntriesByUserId(Long userId);
	
	List<LogEntry> searchByCriteria(Long userId, String startDate, String finalDate);
}
