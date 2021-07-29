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
	
	User findUserByUsername(String username);
	
	User findUserByEmail(String email);
	
	User findUserByActivationCode(String activationCode);
	
	Boolean existsUserByEmail(String email);

	@Query("select u from User u where u.phone = :phone and u.email = :email")
	User findUserByPhoneAndEmail(@Param("phone") String phone,
	                             @Param("email") String email);
	
	@Query(value = "SELECT user.*, role.* FROM user"
			+ " INNER JOIN role ON user.role_id = role.id"
			+ " WHERE user.role_id = 1 AND user.name LIKE CONCAT(?,'%') AND user.surname LIKE CONCAT(?,'%') AND"
			+ " user.phone LIKE CONCAT(?,'%') AND user.email LIKE CONCAT(?,'%') ORDER BY registration_date DESC",
	nativeQuery = true)
	List<User> searchByCriteria(@Param("name") String name,
	                            @Param("surname") String surname,
	                            @Param("phone") String phone,
	                            @Param("email") String email);
}
