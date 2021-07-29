package com.webproject.pms.model.dao;

import com.webproject.pms.model.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDao extends JpaRepository<Payment, Long> {
	
	List<Payment> findPaymentsByAccount_AccountId(Long accountId);
	
	List<Payment> findPaymentsByUserId(Long userId);
	
	@Query(value = "SELECT payment.* FROM payment"
			+ " INNER JOIN account ON payment.account_account_id = account.account_id"
			+ " WHERE account.user_user_id = ? ORDER BY payment_id DESC LIMIT 3",
			nativeQuery = true)
	List<Payment> findLastPaymentsByAccountUserId(Long userId);
	
	@Query(value = "SELECT payment.* FROM payment"
			+ " INNER JOIN account ON payment.account_account_id = account.account_id"
			+ " WHERE account.user_user_id = ? AND is_outgoing = ? AND"
			+ " date BETWEEN STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s') AND"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s)') ORDER BY date DESC;",
			nativeQuery = true)
	List<Payment> searchByCriteria(Long userId, Boolean isOutgoing, String startDate, String finalDate);
	
	@Query(value =  "SELECT payment.* FROM payment"
			+ " INNER JOIN account ON payment.account_account_id = account.account_id"
			+ " WHERE account.user_user_id = ? AND is_outgoing = ? AND date BETWEEN"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s') AND"
			+ " CURRENT_TIMESTAMP() ORDER BY date DESC;",
	nativeQuery = true)
	List<Payment> searchByCriteriaAndFinalDateAsCurrentTimestamp(Long userId, Boolean isOutgoing, String startDate);
	
	@Query(value = "SELECT payment.* FROM payment"
			+ " INNER JOIN account ON payment.account_account_id = account.account_id"
			+ " WHERE account.user_user_id = ? AND date BETWEEN"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s') AND"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s)') ORDER BY date DESC;",
			nativeQuery = true)
	List<Payment> searchByCriteriaWithoutIsOutgoing(Long userId, String startDate, String finalDate);
	
	@Query(value = "SELECT payment.* FROM payment " +
			"INNER JOIN account ON payment.account_account_id = account.account_id " +
			"WHERE account.user_user_id = ? AND date BETWEEN " +
			"STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s') AND " +
			"CURRENT_TIMESTAMP() ORDER BY date DESC;",
	nativeQuery = true)
	List<Payment> searchByCriteriaWithoutIsOutgoingAndFinalDateAsCurrentTimestamp(Long userId, String startDate);
}
