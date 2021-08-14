package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.BankCardDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.service.BankCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BankCardServiceImpl implements BankCardService {

    private final BankCardDao bankCardDao;
    private final ActionLogServiceImpl actionLogService;
    private final StringBuilder stringBuilder = new StringBuilder();
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
    public Boolean addNewBankCard(BankCard bankCard, Account account) {

        SimpleDateFormat formatter = new SimpleDateFormat("MM/yy");
        Date date = null;

        try {
            date = formatter.parse(bankCard.getMonth() + "/" + bankCard.getYear());
        } catch (ParseException e) {
            LOGGER.error("ParseException: " + e.getMessage());
        }

        if (bankCardDao.findBankCardByNumber(bankCard.getNumber()) != null || account == null) {
            actionLogService.createLog("ERROR: Unsuccessful attempt to attach a card", account.getUser());
            LOGGER.error("ERROR: Unsuccessful attempt to attach a card");
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
                .append("]")));
        return true;
    }

    @Override
    public Boolean blockCard(BankCard bankCard) {

        if (bankCard != null) {
            bankCard.setActive(false);
            bankCardDao.save(bankCard);

            actionLogService.createLog(String.valueOf(stringBuilder.append("BLOCKED: Card [")
                    .append(bankCard.getNumber())
                    .append("]")), bankCard.getAccount().getUser());

            LOGGER.info(String.valueOf(stringBuilder.append("BLOCKED: Card [")
                    .append(bankCard.getNumber())
                    .append("]")));
            return true;
        }
        return false;
    }

    @Override
    public Boolean unblockCard(BankCard bankCard) {

        if (bankCard != null) {
            bankCard.setActive(true);
            bankCardDao.save(bankCard);

            actionLogService.createLog(String.valueOf(stringBuilder.append("UNBLOCKED: Card [")
                    .append(bankCard.getNumber())
                    .append("]")), bankCard.getAccount().getUser());

            LOGGER.info(String.valueOf(stringBuilder.append("UNBLOCKED: Card [")
                    .append(bankCard.getNumber())
                    .append("]")));
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteCard(BankCard bankCard) {

        actionLogService.createLog(String.valueOf(stringBuilder.append("DETACHED: Card [")
                .append(bankCard.getNumber())
                .append("]")), bankCard.getAccount().getUser());

        LOGGER.info(String.valueOf(stringBuilder.append("DETACHED: Card [")
                .append(bankCard.getNumber())
                .append("]")));
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
