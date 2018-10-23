package org.tishkevich.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tishkevich.entities.VkontakteAccount;

public interface UserRepo extends JpaRepository<VkontakteAccount, Long> {
	VkontakteAccount findByUsername(String username);
}
