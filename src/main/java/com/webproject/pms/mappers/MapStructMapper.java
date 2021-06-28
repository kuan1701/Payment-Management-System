package com.webproject.pms.mappers;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.model.entities.dto.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
	
	/**
	 * BankCard mapper
	 */
	BankCardGetDto bankCardToBankCardGetDto(BankCard bankCard);
	
	BankCardPostDto bankCardToBankCardPostDto(BankCard bankCard);
	
	List<BankCardGetDto> bankCardsToBankCardGetDtos(List<BankCard> bankCards);
	
	/**
	 * Payment mapper
	 */
	Payment paymentPostDtoToPayment(PaymentPostDto paymentPostDto);
	
	PaymentGetDto paymentToPaymentGetDto(Payment payment);
	
	List<PaymentGetDto> paymentsToPaymentGetDtos(List<Payment> payments);
	
	/**
	 * Account mapper
	 */
	AccountDto accountToAccountDto(Account account);
	
	Account accountDtoToAccount(AccountDto accountDto);
	
	List<AccountDto> accountsToAccountDtos(List<Account> accounts);
	
	/**
	 * User mapper
	 */
	User userPostDtoToUser(UserPostDto userPostDto);
	
	User userGetDtoToUser(UserGetDto userGetDto);
	
	//UserPostDto userToUserPostDto(User user);
	
	UserGetDto userToUserGetDto(User user);
	
	List<UserGetDto> usersToUserGetDtos(List<User> users);
}
