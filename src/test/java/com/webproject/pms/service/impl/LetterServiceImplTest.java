package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.LetterDao;
import com.webproject.pms.model.entities.Letter;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
class LetterServiceImplTest {

    private User user;
    private Letter letter;

    @Autowired
    private LetterServiceImpl letterService;

    @MockBean
    private LetterDao letterDao;
    @MockBean
    private Principal principal;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId(1L);

        letter = new Letter();
        letter.setLetterId(1L);
        letter.setProcessed(false);
        letter.setUser(user);
        letter.setDescription("some text");
        letter.setDate("08/08/2021 21:15:16");
    }

    @Test
    void addNewLetter() {

        Boolean letterIsCreated = letterService.addNewLetter(letter, principal);
        assertTrue(letterIsCreated);
        assertNotNull(letter.getDate());
        assertNotNull(letter.getDescription());
        assertFalse(letter.getProcessed());
        verify(letterDao, times(1)).save(letter);
    }

    @Test
    void updateLetterByLetterId() {

        Mockito.doReturn(letter)
                .when(letterDao)
                .getById(letter.getLetterId());

        Boolean letterIsUpdated = letterService.updateLetterByLetterId(letter.getLetterId());
        assertTrue(letterIsUpdated);
        assertTrue(letter.getProcessed());
        verify(letterDao, times(1)).save(letter);
    }

    @Test
    void findLetterByLetterId() {

        letterService.findLetterByLetterId(letter.getLetterId());
        verify(letterDao, times(1)).getById(letter.getLetterId());
    }

    @Test
    void findUnprocessedLetters() {

        assertNotNull(letterService.findUnprocessedLetters());
    }

    @Test
    void findAllLetters() {

        assertNotNull(letterService.findAllLetters());
        verify(letterDao, times(1)).findAll();
    }

    @Test
    void searchByCriteria() {

        String startDate = "05/08/2021";
        String finalDate = "09/08/2021";
        String typeQuestion = "some question";

        List<Letter> letterList = letterService.searchByCriteria(typeQuestion, startDate, finalDate);
        assertNotNull(letterList);
    }

    @Test
    void testSearchByCriteria() {

        String startDate = "05/08/2021";
        String finalDate = "09/08/2021";

        List<Letter> letterList = letterService.searchByCriteria(startDate, finalDate);
        assertNotNull(letterList);
    }
}