package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.ActionLogDao;
import com.webproject.pms.model.entities.LogEntry;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.ActionLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@Transactional
public class ActionLogServiceImpl implements ActionLogService {

    private final ActionLogDao actionLogDao;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public ActionLogServiceImpl(ActionLogDao actionLogDao) {
        this.actionLogDao = actionLogDao;
    }

    @Override
    public Boolean createLog(String description, User user) {

        LogEntry logEntry = new LogEntry();
        logEntry.setDescription(description);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        logEntry.setDate(formatter.format(new Date()));
        logEntry.setUser(user);
        actionLogDao.save(logEntry);
        return true;
    }

    @Override
    public Boolean save(LogEntry logEntry) {

        actionLogDao.save(logEntry);
        return true;
    }

    @Override
    public Boolean clearActionLog(Long userId) {

        if (userId != null) {
            actionLogDao.deleteAllByUser_UserId(userId);
            return true;
        }
        return false;
    }

    @Override
    public LogEntry findLogEntryByLogEntryId(Long logEntryId) {
        return actionLogDao.getById(logEntryId);
    }

    @Override
    public List<LogEntry> findLogEntriesByUserId(Long userId) {
        return actionLogDao.findLogEntriesByUser_UserId(userId);
    }

    @Override
    public List<LogEntry> searchByCriteria(Long userId, String startDate, String finalDate) {

        List<LogEntry> logEntries;

        if (StringUtils.isEmpty(startDate)) {
            startDate = "01/01/2020 00:00:00";
        }
        else {
            startDate += " 00:00:00";
        }

        if (StringUtils.isEmpty(finalDate)) {
            logEntries = actionLogDao.searchByCriteria(userId, startDate);
        }
        else {
            finalDate += " 23:59:59";
            logEntries = actionLogDao.searchByCriteria(userId, startDate, finalDate);
        }
        return logEntries;
    }
}