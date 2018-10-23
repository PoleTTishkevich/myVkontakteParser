package org.tishkevich.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tishkevich.entities.UserRole;

public interface RoleRepo extends JpaRepository<UserRole, Long> {
	
}