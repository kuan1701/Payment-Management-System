package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.ActionLogDao;
import com.webproject.pms.model.entities.LogEntry;
import com.webproject.pms.model.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class ActionLogServiceImplTest {

    private User user;
    private LogEntry logEntry;

    @Autowired
    private ActionLogServiceImpl actionLogService;

    @MockBean
    Principal principal;
    @MockBean
    ActionLogDao actionLogDao;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId(1L);

        logEntry = new LogEntry();
        logEntry.setLogEntryId(1L);
        logEntry.setDescription("someText");
        logEntry.setDate("07/08/2021 15:58:30");
        logEntry.setUser(user);
    }

    @Test
    void createLog() {

        String description = "someText";
        Boolean actionLogIsCreated = actionLogService.createLog(description, user);
        Assertions.assertTrue(actionLogIsCreated);
        Assertions.assertNotNull(logEntry.getUser());
        Assertions.assertNotNull(logEntry.getDescription());
    }

    @Test
    void save() {

        Boolean actionLogSaved = actionLogService.save(logEntry);
        Assertions.assertTrue(actionLogSaved);
        Mockito.verify(actionLogDao, Mockito.times(1)).save(logEntry);
    }

    @Test
    void clearActionLog() {

       Boolean logEntriesRemoved = actionLogService.clearActionLog(user.getUserId());
       Assertions.assertTrue(logEntriesRemoved);
    }

    @Test
    void findLogEntryByLogEntryId() {

        actionLogService.findLogEntryByLogEntryId(logEntry.getLogEntryId());
        Assertions.assertNotNull(logEntry.getUser());
        Assertions.assertNotNull(logEntry.getDate());
        Assertions.assertNotNull(logEntry.getDescription());

        Mockito.verify(actionLogDao, Mockito.times(1))
                .getById(logEntry.getLogEntryId());
    }

    @Test
    void findLogEntriesByUserId() {

        actionLogService.findLogEntriesByUserId(logEntry.getUser().getUserId());
        Mockito.verify(actionLogDao, Mockito.times(1))
                .findLogEntriesByUser_UserId(logEntry.getUser().getUserId());
    }

    @Test
    void searchByCriteria() {
        String startDate = "05/08/2021";
        String finalDate = "09/08/2021";

        List<LogEntry> logEntryList = actionLogService.searchByCriteria(user.getUserId(), startDate, finalDate);
        Assertions.assertNotNull(logEntryList);
    }
}