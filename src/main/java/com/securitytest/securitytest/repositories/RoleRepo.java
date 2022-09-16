package com.securitytest.securitytest.repositories;

import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Integer> {
    Role findByName(RoleName roleName);
}
