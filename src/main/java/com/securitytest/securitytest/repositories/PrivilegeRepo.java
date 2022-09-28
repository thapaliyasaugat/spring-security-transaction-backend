package com.securitytest.securitytest.repositories;

import com.securitytest.securitytest.models.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepo extends JpaRepository<Privilege,Integer> {
    Privilege findByName(String name);
}
