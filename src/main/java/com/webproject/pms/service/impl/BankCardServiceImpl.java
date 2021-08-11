package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
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
    private final MapStructMapper mapStructMapper;
    private final ActionLogServiceImpl actionLogService;
    private static final Logger LOGGER = LogManager.getLogger(BankCardService.class);

    @Autowired
    public BankCardServiceImpl(BankCardDao bankCardDao,
                               MapStructMapper mapStructMapper,
                               ActionLogServiceImpl actionLogService
    ) {
        this.bankCardDao = bankCardDao;
        this.mapStructMapper = mapStructMapper;
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

        if (bankCardDao.findBankCardByNumber(bankCard.getNumber()) != null
                || account == null
        ) {
            actionLogService.createLog("ERROR: Unsuccessful attempt to attach a card", account.getUser());
            LOGGER.error("ERROR: Unsuccessful attempt to attach a card");
            return false;
        }
        bankCard.setAccount(account);
        bankCard.setActive(true);
        bankCard.setValidity(formatter.format(date));
        bankCardDao.save(bankCard);

        actionLogService.createLog("ATTACHED: Card [" + bankCard.getNumber() + "]", account.getUser());
        LOGGER.info("ATTACHED: Card [" + bankCard.getNumber() + "]");
        return true;
    }

    @Override
    public Boolean blockCard(BankCard bankCard) {

        if (bankCard != null) {
            bankCard.setActive(false);
            bankCardDao.save(bankCard);

            actionLogService.createLog("BLOCKED: Card [" + bankCard.getNumber() + "]", bankCard.getAccount().getUser());
            LOGGER.info("BLOCKED: Card [" + bankCard.getNumber() + "]");
            return true;
        }
        return false;
    }

    @Override
    public Boolean unblockCard(BankCard bankCard) {

        if (bankCard != null) {
            bankCard.setActive(true);
            bankCardDao.save(bankCard);

            actionLogService.createLog("UNBLOCKED: Card [" + bankCard.getNumber() + "]", bankCard.getAccount().getUser());
            LOGGER.info("UNBLOCKED: Card [" + bankCard.getNumber() + "]");
            return true;

        }
        return false;
    }

    @Override
    public Boolean deleteCard(BankCard card) {

        actionLogService.createLog("DETACHED: Card [" + card.getNumber() + "]", card.getAccount().getUser());
        LOGGER.info("DELETED: Card [" + card.getNumber() + "]");
        bankCardDao.delete(card);
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
