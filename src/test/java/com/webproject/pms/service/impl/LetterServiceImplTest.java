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
        Assertions.assertTrue(letterIsCreated);
        Assertions.assertNotNull(letter.getDate());
        Assertions.assertNotNull(letter.getDescription());
        Assertions.assertFalse(letter.getProcessed());
        Mockito.verify(letterDao, Mockito.times(1)).save(letter);
    }

    @Test
    void updateLetterByLetterId() {

        Mockito.doReturn(letter)
                .when(letterDao)
                .getById(letter.getLetterId());

        Boolean letterIsUpdated = letterService.updateLetterByLetterId(letter.getLetterId());
        Assertions.assertTrue(letterIsUpdated);
        Assertions.assertTrue(letter.getProcessed());
        Mockito.verify(letterDao, Mockito.times(1)).save(letter);
    }

    @Test
    void findLetterByLetterId() {

        letterService.findLetterByLetterId(letter.getLetterId());
        Mockito.verify(letterDao, Mockito.times(1)).getById(letter.getLetterId());
    }

    @Test
    void findUnprocessedLetters() {

        Assertions.assertNotNull(letterService.findUnprocessedLetters());
    }

    @Test
    void findAllLetters() {

        Assertions.assertNotNull(letterService.findAllLetters());
        Mockito.verify(letterDao, Mockito.times(1)).findAll();
    }

    @Test
    void searchByCriteria() {

        String startDate = "05/08/2021";
        String finalDate = "09/08/2021";
        String typeQuestion = "some question";

        List<Letter> letterList = letterService.searchByCriteria(typeQuestion, startDate, finalDate);
        Assertions.assertNotNull(letterList);
    }

    @Test
    void testSearchByCriteria() {

        String startDate = "05/08/2021";
        String finalDate = "09/08/2021";

        List<Letter> letterList = letterService.searchByCriteria(startDate, finalDate);
        Assertions.assertNotNull(letterList);
    }
}