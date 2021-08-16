package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.dao.BankCardDao;
import com.webproject.pms.model.dao.PaymentDao;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.Payment;
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
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PaymentServiceImplTest {

    private User user;
    private BankCard card;
    private Account accountFrom;
    private Account accountTo;
    private Payment outgoingPayment;
    BigDecimal amount = new BigDecimal("100.00");

    @Autowired
    private PaymentServiceImpl paymentService;

    private final PaymentServiceImpl paymentServiceMock = Mockito.mock(PaymentServiceImpl.class);

    @MockBean
    private UserDao userDao;
    @MockBean
    private AccountDao accountDao;
    @MockBean
    private BankCardDao cardDao;
    @MockBean
    private PaymentDao paymentDao;
    @MockBean
    private Model model;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId(1L);

        accountFrom = new Account();
        accountFrom.setAccountId(1L);
        accountFrom.setNumber("98765432198765432198");
        accountFrom.setBlocked(false);
        accountFrom.setBalance(new BigDecimal("10000.00"));

        accountTo = new Account();
        accountTo.setAccountId(2L);
        accountTo.setNumber("98765432198765432199");
        accountTo.setBlocked(false);
        accountTo.setBalance(new BigDecimal("10000.00"));

        outgoingPayment = new Payment();
        outgoingPayment.setPaymentId(1L);
        outgoingPayment.setSenderNumber(accountFrom.getNumber());
        outgoingPayment.setRecipientNumber(accountTo.getNumber());
        outgoingPayment.setAccount(accountFrom);

        card = new BankCard();
        card.setCardId(1L);
        card.setNumber("1234 5678 9101 1121");
    }

    @Test
    void makePaymentOnAccount() {

        String appointment = "some text";
        BigDecimal amount = new BigDecimal("100.00");

        doReturn(user).when(userDao).getById(user.getUserId());
        doReturn(accountFrom).when(accountDao).getById(accountFrom.getAccountId());
        doReturn(accountTo).when(accountDao).findAccountByNumber(accountTo.getNumber());
        doReturn(true).when(paymentServiceMock).checkAvailableAmount(accountFrom, amount);

        doNothing().when(paymentServiceMock).transaction(accountFrom, accountTo, amount);

        Boolean paymentIsCreated = paymentService.makePaymentOnAccount(
                accountFrom.getAccountId(),
                accountTo.getNumber(),
                amount,
                appointment,
                model,
                user);

        assertTrue(paymentIsCreated);
    }

    @Test
    void makePaymentOnCard() {

        String appointment = "some text";
        BigDecimal amount = new BigDecimal("100.00");

        doReturn(user).when(userDao).getById(user.getUserId());
        doReturn(card).when(cardDao).findBankCardByNumber(card.getNumber());
        doReturn(accountFrom).when(accountDao).getById(accountFrom.getAccountId());
        doReturn(true).when(paymentServiceMock).checkAvailableAmount(accountFrom, amount);

        doNothing().when(paymentServiceMock).transaction(accountFrom, card.getNumber(), amount);

        Boolean paymentIsCreated = paymentService.makePaymentOnCard(
                accountFrom.getAccountId(),
                card.getNumber(),
                amount,
                appointment,
                model,
                user);

        assertTrue(paymentIsCreated);
    }

    @Test
    void checkAvailableAmount() {

        Boolean accountFromChecked = paymentService.checkAvailableAmount(accountFrom, amount);
        assertTrue(accountFromChecked);

        Boolean accountToChecked = paymentService.checkAvailableAmount(accountTo, amount);
        assertTrue(accountToChecked);
    }

    @Test
    void transaction() {

        BigDecimal amount = new BigDecimal("100.00");

        doReturn(accountFrom).when(accountDao).getById(accountFrom.getAccountId());
        doReturn(accountTo).when(accountDao).getById(accountTo.getAccountId());

       paymentService.transaction(accountFrom, accountTo, amount);
       verify(accountDao, times(1)).save(accountFrom);
       verify(accountDao, times(1)).save(accountTo);
    }

    @Test
    void testTransaction() {

        BigDecimal amount = new BigDecimal("100.00");

        doReturn(accountFrom).when(accountDao).getById(accountFrom.getAccountId());
        doReturn(card).when(cardDao).getById(card.getCardId());

        paymentService.transaction(accountFrom, card.getNumber(), amount);
        verify(accountDao, times(1)).save(accountFrom);
    }

    @Test
    void findPaymentByPaymentId() {

        paymentService.findPaymentByPaymentId(outgoingPayment.getPaymentId());
        verify(paymentDao, times(1)).getById(outgoingPayment.getPaymentId());
    }

    @Test
    void findAllPaymentsByAccountId() {

        paymentService.findAllPaymentsByAccountId(accountFrom.getAccountId());
        verify(paymentDao, times(1))
                .findPaymentsByAccount_AccountId(accountFrom.getAccountId());
    }

    @Test
    void findAllPaymentsByUserId() {

        paymentService.findAllPaymentsByUserId(user.getUserId());
        verify(paymentDao, times(1))
                .findPaymentsByUserIdOrderByDateDesc(user.getUserId());
    }

    @Test
    void searchByCriteria() {

        String startDate = "05/08/2021";
        String finalDate = "09/08/2021";

        List<Payment> paymentList = paymentService.searchByCriteria(user.getUserId(), true, startDate, finalDate);
        assertNotNull(paymentList);
    }

    @Test
    void searchByCriteriaOutgoingFalse() {

        String startDate = "05/08/2021";
        String finalDate = "09/08/2021";

        List<Payment> paymentList = paymentService.searchByCriteriaOutgoingFalse(user.getUserId(), false, startDate, finalDate);
        assertNotNull(paymentList);
    }

    @Test
    void searchByCriteriaWithoutOutgoing() {

        String startDate = "05/08/2021";
        String finalDate = "09/08/2021";

        List<Payment> paymentList = paymentService.searchByCriteriaWithoutOutgoing(user.getUserId(), startDate, finalDate);
        assertNotNull(paymentList);
    }
}