package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.BankCardDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
class BankCardServiceImplTest {

    private Account account;
    private BankCard bankCard;

    @Autowired
    private BankCardServiceImpl bankCardService;

    @MockBean
    BankCardDao bankCardDao;

    @BeforeEach
    void setUp() throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("MM/yy");

        account = new Account();
        account.setAccountId(1L);
        account.setNumber("12345678912345678912");

        bankCard = new BankCard();
        bankCard.setCardId(1L);
        bankCard.setActive(true);
        bankCard.setAccount(account);
        bankCard.setMonth("08");
        bankCard.setYear("23");
        Date date = formatter.parse(bankCard.getMonth() + "/" + bankCard.getYear());
        bankCard.setValidity(formatter.format(date));
        bankCard.setNumber("1111 1111 1111 1111");
    }

    @Test
    void save() {

        Boolean cardAttached = bankCardService.save(bankCard);
        assertTrue(cardAttached);
        verify(bankCardDao, times(1)).save(bankCard);
    }

    @Test
    void addNewBankCard() {

        Boolean cardAttached = bankCardService.addNewBankCard(bankCard, account);
        assertTrue(cardAttached);
        assertTrue(bankCard.getActive());
        assertNotNull(bankCard.getNumber());
        assertNotNull(bankCard.getAccount());
        verify(bankCardDao, times(1)).save(bankCard);
    }

    @Test
    void blockCard() {

        Boolean cardIsBlocked = bankCardService.blockCard(bankCard);
        assertTrue(cardIsBlocked);
        assertFalse(bankCard.getActive());
        verify(bankCardDao, times(1)).save(bankCard);
    }

    @Test
    void unblockCard() {

        Boolean cardIsUnblocked = bankCardService.unblockCard(bankCard);
        assertTrue(cardIsUnblocked);
        assertTrue(bankCard.getActive());
        verify(bankCardDao, times(1)).save(bankCard);
    }

    @Test
    void deleteCard() {

        Boolean cardIsDeleted = bankCardService.deleteCard(bankCard);
        assertTrue(cardIsDeleted);
    }

    @Test
    void findCardByCardId() {

        bankCardService.findCardByCardId(bankCard.getCardId());
        verify(bankCardDao, times(1)).getById(bankCard.getCardId());
    }

    @Test
    void findCardsByAccountId() {

        bankCardService.findCardsByAccountId(bankCard.getAccount().getAccountId());
        verify(bankCardDao, times(1))
                .findBankCardsByAccount_AccountId(bankCard.getAccount().getAccountId());
    }
}