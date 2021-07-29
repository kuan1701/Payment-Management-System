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
	
	@Query(value= "SELECT * FROM account WHERE user_user_id = ?"
			+ " AND is_deleted = 0 AND is_blocked = 0 ORDER BY account_id ASC",
	nativeQuery = true)
	List<Account> findAllActivateAccountByUserId(@Param("userId") Long userId);
	
	@Query(value = "SELECT * FROM account"
			+ " WHERE user_user_id = ? AND is_deleted = 0 AND"
			+ " number LIKE CONCAT(?, '%') AND balance >= ? AND balance <= ? AND"
			+ " currency LIKE CONCAT(?, '%') ORDER BY account_id ASC",
			nativeQuery = true)
	List<Account> searchByCriteria(@Param("userId") Long userId,
	                               @Param("number") String number,
	                               @Param("min_value") String min_value,
	                               @Param("max_value") String max_value,
	                               @Param("currency") String currency);
	
	@Query(value = "SELECT * FROM account"
			+ " WHERE is_deleted = 0 AND number LIKE CONCAT(?, '%') AND"
			+ " balance >= ? AND balance <= ? AND"
			+ " currency LIKE CONCAT(?, '%') ORDER BY account_id ASC",
	nativeQuery = true)
	List<Account> searchByCriteriaWithoutId(@Param("number") String number,
	                                        @Param("min_value") String min_value,
	                                        @Param("max_value") String max_value,
	                                        @Param("currency") String currency);
}
