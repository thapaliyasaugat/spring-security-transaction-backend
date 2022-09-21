package com.securitytest.securitytest.repositories;

import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Integer> {
    User findByEmail(String email);
    @Query(value = "select * from user_roles where user_id=:id",nativeQuery = true)
    List<Role> getUserRoles(int id);

}
