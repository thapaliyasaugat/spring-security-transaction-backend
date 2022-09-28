package com.securitytest.securitytest.configuration;

import com.securitytest.securitytest.models.Privilege;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.RoleName;
import com.securitytest.securitytest.models.User;
import com.securitytest.securitytest.repositories.PrivilegeRepo;
import com.securitytest.securitytest.repositories.RoleRepo;
import com.securitytest.securitytest.repositories.UserRepo;
import com.securitytest.securitytest.resource.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PrivilegeRepo privilegeRepo;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepo userRepo, RoleRepo roleRepo, PrivilegeRepo privilegeRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.privilegeRepo = privilegeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
      log.info("Application Listener Listening...");
        if(alreadySetup) return;
        Privilege createRolePrivilege = createPrivilegeIfNotFound("CREATE_ROLE_PRIVILEGE");
        Privilege transactionPrivilege = createPrivilegeIfNotFound("TRANSACTION_PRIVILEGE");
        Set<Privilege> adminPrivilege = new HashSet<>(Arrays.asList(createRolePrivilege,transactionPrivilege));
        createRoleIfNotFound("ADMIN",adminPrivilege);
        createRoleIfNotFound("CUSTOMER",new HashSet<>(Arrays.asList(transactionPrivilege)));

        Role adminRole = roleRepo.findByName(RoleName.ADMIN);
        User user = new User();
        user.setUserName("Saugat Admin");
        user.setEmail("saugat@email.com");
        user.setPassword(passwordEncoder.encode("saugat@123"));
        user.setBalance(1000000.00);
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(new HashSet<>(Arrays.asList(adminRole)));
        userRepo.save(user);
        alreadySetup = true;

    }
    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepo.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepo.save(privilege);
            log.info("privilege created :: {}",name);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Set<Privilege> privileges){
        Role role = roleRepo.findByName(RoleName.valueOf(name));
        if(role == null){
            role = new Role();
            role.setPrivileges(privileges);
            roleRepo.save(role);
            log.info("role created :: {} with privileges :: {}", name, privileges);
        }
        return role;
        }
}
