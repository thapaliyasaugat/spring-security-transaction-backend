package com.securitytest.securitytest.repositories;

import com.securitytest.securitytest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
    User findByEmail(String email);

}
