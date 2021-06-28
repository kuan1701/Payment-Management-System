package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.ActionLogDao;
import com.webproject.pms.model.entities.LogEntry;
import com.webproject.pms.service.ActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class ActionLogServiceImpl implements ActionLogService {
	
	private final ActionLogDao actionLogDao;
	
	@Autowired
	public ActionLogServiceImpl(ActionLogDao actionLogDao) {
		
		this.actionLogDao = actionLogDao;
	}
	
	@Override
	@Transactional
	public LogEntry save(LogEntry logEntry) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		logEntry.setDate(formatter.format(new Date()));
		
		return actionLogDao.save(logEntry);
	}
	
	@Override
	@Transactional
	public Boolean clearActionLog(Long userId) {
		
		if (userId != null) {
			actionLogDao.deleteAllByUserId(userId);
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional
	public LogEntry findLogEntryByLogEntryId(Long logEntryId) {
		return actionLogDao.getOne(logEntryId);
	}
	
	@Override
	@Transactional
	public List<LogEntry> findLogEntriesByUserId(Long userId) {
		return actionLogDao.findLogEntriesByUser_UserId(userId);
	}
	
	@Override
	@Transactional
	public List<LogEntry> searchByCriteria(Long userId, String startDate, String finalDate) {
		return actionLogDao.searchByCriteria(userId, startDate, finalDate);
	}
}
