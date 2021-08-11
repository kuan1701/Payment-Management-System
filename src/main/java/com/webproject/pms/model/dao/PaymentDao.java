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
	
	@Query(value = "SELECT * FROM payment"
			+ " WHERE user_id = ? AND is_outgoing = ? AND"
			+ " date BETWEEN STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s') AND"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s)') ORDER BY date DESC;",
			nativeQuery = true)
	List<Payment> searchByCriteria(Long userId, Boolean isOutgoing, String startDate, String finalDate);
	
	@Query(value =  "SELECT * FROM payment"
			+ " WHERE user_id = ? AND is_outgoing = ? AND date BETWEEN"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s') AND"
			+ " CURRENT_TIMESTAMP() ORDER BY date DESC;",
			nativeQuery = true)
	List<Payment> searchByCriteriaAndFinalDateAsCurrentTimestamp(Long userId, Boolean isOutgoing, String startDate);
	
	@Query(value = "SELECT * FROM payment"
			+ " WHERE user_id = ? AND date BETWEEN"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s') AND"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s)') ORDER BY date DESC;",
			nativeQuery = true)
	List<Payment> searchByCriteriaWithoutIsOutgoing(Long userId, String startDate, String finalDate);
	
	@Query(value = "SELECT * FROM payment"
			+ " WHERE user_id = ? AND date BETWEEN"
			+ " STR_TO_DATE(?, '%d/%m/%Y %H:%i:%s') AND"
			+ " CURRENT_TIMESTAMP() ORDER BY date DESC;",
	nativeQuery = true)
	List<Payment> searchByCriteriaWithoutIsOutgoingAndFinalDateAsCurrentTimestamp(Long userId, String startDate);
}
