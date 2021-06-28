package com.webproject.pms.model.dao;

import com.webproject.pms.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
	
	User findUserByPhone(String phone);
	
	User findUserByLogin(String login);
	
	User findUserByEmail(String email);
	
	User findUserByActivationCode(String code);
	
	User findUserByGoogleUserName(String googleUsername);
	
	@Query("select u from User u where u.phone = :phone and u.email = :email")
	User findUserByPhoneAndEmail(@Param("phone") String phone,
	                             @Param("email") String email);
	
	@Query("select u from User u"
			+ " where u.name = :name and u.surname = :surname"
			+ " and u.phone = :phone and u.email = :email")
	List<User> searchByCriteria(@Param("name") String name,
	                            @Param("surname") String surname,
	                            @Param("phone") String phone,
	                            @Param("email") String email);
}
