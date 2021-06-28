package com.webproject.pms.model.dao;

import com.webproject.pms.model.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {
	
	Account findAccountByNumber(String number);
	
	List<Account> findAllAccountsByUser_UserId(Long userId);
	
	@Query("select a from Account a inner join User u on u.userId = :userId and"
			+ " a.isDeleted = false and a.number like concat(:number, '%')"
			+ " and a.balance >= :min_value and a.balance <= :max_value"
			+ " and a.currency like concat(:currency, '%') order by a.accountId asc")
	List<Account> searchByCriteria(@Param("userId") Long userId,
	                               @Param("number") String number,
	                               @Param("min_value") String min_value,
	                               @Param("max_value") String max_value,
	                               @Param("currency") String currency);
	
	@Query("select a from Account a where a.isDeleted = false"
			+ " and a.number like concat(:number, '%')"
			+ " and a.balance >= :min_value and a.balance <= :max_value"
			+ " and a.currency like concat(:currency, '%') order by a.accountId asc")
	List<Account> searchByCriteria(@Param("number") String number,
	                               @Param("min_value") String min_value,
	                               @Param("max_value") String max_value,
	                               @Param("currency") String currency);
}
