package com.webproject.pms.model.dao;

import com.webproject.pms.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long> {
}
