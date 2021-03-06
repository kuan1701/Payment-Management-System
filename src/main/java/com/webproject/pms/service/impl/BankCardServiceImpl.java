package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.BankCardDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.service.BankCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BankCardServiceImpl implements BankCardService {

    private final BankCardDao bankCardDao;
    private final ActionLogServiceImpl actionLogService;
    private static final Logger LOGGER = LogManager.getLogger(BankCardService.class);

    @Autowired
    public BankCardServiceImpl(BankCardDao bankCardDao,
                               ActionLogServiceImpl actionLogService
    ) {
        this.bankCardDao = bankCardDao;
        this.actionLogService = actionLogService;
    }

    @Override
    public Boolean save(BankCard bankCard) {

        bankCardDao.save(bankCard);
        return true;
    }

    @Override
    @Transactional
    public Boolean addNewBankCard(BankCard bankCard, Account account) {

        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/yy");
        formatter.setLenient(false);
        Date date = null;
        Boolean expired = true;

        try {
            date = formatter.parse(bankCard.getMonth() + "/" + bankCard.getYear());
            expired = date.before(new Date());
        } catch (ParseException e) {
            LOGGER.error("ParseException: " + e.getMessage());
        }

        if (bankCardDao.findBankCardByNumber(bankCard.getNumber()) != null || account == null || expired) {
            actionLogService.createLog("ERROR: Unsuccessful attempt to attach a card", account.getUser());
            LOGGER.error("ERROR: Unsuccessful attempt to attach a card\n");
            return false;
        }
        bankCard.setAccount(account);
        bankCard.setActive(true);
        bankCard.setValidity(formatter.format(date));
        bankCardDao.save(bankCard);

        actionLogService.createLog(String.valueOf(stringBuilder.append("ATTACHED: Card [")
                .append(bankCard.getNumber())
                .append("]")), account.getUser());

        LOGGER.info(String.valueOf(stringBuilder.append("ATTACHED: Card [")
                .append(bankCard.getNumber())
                .append("]\n")));
        return true;
    }

    @Override
    @Transactional
    public Boolean blockCard(BankCard bankCard) {

        StringBuilder stringBuilder = new StringBuilder();
        if (bankCard != null) {
            bankCard.setActive(false);
            bankCardDao.save(bankCard);

            actionLogService.createLog(String.valueOf(stringBuilder.append("BLOCKED: Card [")
                    .append(bankCard.getNumber())
                    .append("]")), bankCard.getAccount().getUser());

            LOGGER.info(String.valueOf(stringBuilder.append("BLOCKED: Card [")
                    .append(bankCard.getNumber())
                    .append("]\n")));
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean unblockCard(BankCard bankCard) {

        StringBuilder stringBuilder = new StringBuilder();
        if (bankCard != null) {
            bankCard.setActive(true);
            bankCardDao.save(bankCard);

            actionLogService.createLog(String.valueOf(stringBuilder.append("UNBLOCKED: Card [")
                    .append(bankCard.getNumber())
                    .append("]")), bankCard.getAccount().getUser());

            LOGGER.info(String.valueOf(stringBuilder.append("UNBLOCKED: Card [")
                    .append(bankCard.getNumber())
                    .append("]\n")));
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean deleteCard(BankCard bankCard) {

        StringBuilder stringBuilder = new StringBuilder();
        actionLogService.createLog(String.valueOf(stringBuilder.append("DETACHED: Card [")
                .append(bankCard.getNumber())
                .append("]")), bankCard.getAccount().getUser());

        LOGGER.info(String.valueOf(stringBuilder.append("DETACHED: Card [")
                .append(bankCard.getNumber())
                .append("]\n")));
        bankCardDao.delete(bankCard);
        return true;
    }

    @Override
    public BankCard findCardByCardId(Long cardId) {
        return bankCardDao.getById(cardId);
    }

    @Override
    public List<BankCard> findCardsByAccountId(Long accountId) {
        return bankCardDao.findBankCardsByAccount_AccountId(accountId);
    }
}
