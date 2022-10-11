package com.securitytest.securitytest.configuration;

import com.securitytest.securitytest.models.Privilege;
import com.securitytest.securitytest.models.Role;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//@Component
// make spring.jpa.hibernate.ddl-auto = create
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
        Privilege fetchOwnDataPrivilege = createPrivilegeIfNotFound("FETCH_OWN_DATA_PRIVILEGE");
        Privilege fetchAllUsersDataPrivilege = createPrivilegeIfNotFound("FETCH_ALL_USERS_PRIVILEGE");
        Privilege loadBalancePrivilege = createPrivilegeIfNotFound("LOAD_BALANCE_PRIVILEGE");
        Privilege fetchOwnLoadedDataPrivilege = createPrivilegeIfNotFound("FETCH_OWN_LOADED_AMOUNT_DETAIL_PRIVILEGE");
        Privilege fetchAllLoadedAmountDetailPrivilege = createPrivilegeIfNotFound("FETCH_ALL_LOADED_AMOUNT_DETAIL_PRIVILEGE");
        Privilege viewOthersTransactionPrivilege = createPrivilegeIfNotFound("VIEW_OTHERS_TRANSACTIONS_PRIVILEGE");
        Privilege performAndViewOwnTransactionsPrivilege = createPrivilegeIfNotFound("PERFORM_AND_VIEW_OWN_TRANSACTIONS_PRIVILEGE");
        Privilege createAndUpdateNewRolePrivilege = createPrivilegeIfNotFound("CREATE_AND_UPDATE_NEW_ROLE_PRIVILEGE");
        Privilege viewUserRolesPrivilege = createPrivilegeIfNotFound("VIEW_USER_ROLES_PRIVILEGE");
        Privilege blockActivateUserPrivilege = createPrivilegeIfNotFound("BLOCK_ACTIVATE_USER_PRIVILEGE");
        Privilege updateUserRolePrivilege = createPrivilegeIfNotFound("UPDATE_USER_ROLE_PRIVILEGE");

        Set<Privilege> adminPrivilege = new HashSet<>(Arrays.asList(fetchAllLoadedAmountDetailPrivilege,createAndUpdateNewRolePrivilege));
        createRoleIfNotFound("ADMIN",adminPrivilege);

        Set<Privilege> adminSupportPrivilege = new HashSet<>(Arrays.asList(fetchAllUsersDataPrivilege,blockActivateUserPrivilege,updateUserRolePrivilege,viewUserRolesPrivilege));
        createRoleIfNotFound("ADMIN-SUPPORT",adminSupportPrivilege);

        Set<Privilege> transactionPrivilege = new HashSet<>(Collections.singletonList(viewOthersTransactionPrivilege));
        createRoleIfNotFound("TRANSACTION",transactionPrivilege);

        Set<Privilege> customerPrivilege = new HashSet<>(Arrays.asList(fetchOwnDataPrivilege,loadBalancePrivilege,fetchOwnLoadedDataPrivilege,performAndViewOwnTransactionsPrivilege));
        createRoleIfNotFound("CUSTOMER",customerPrivilege);

        Role adminRole = roleRepo.findByName("ADMIN");
        log.info("admin role , {}",adminRole);
        User user = new User();
        user.setUserName("Saugat Admin");
        user.setEmail("saugat@email.com");
        user.setPassword(passwordEncoder.encode("saugat@123"));
        user.setBalance(1000000.00);
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
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
    void createRoleIfNotFound(String name, Set<Privilege> privileges){
        Role role = roleRepo.findByName(name);
        if(role == null){
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepo.save(role);
            log.info("role created :: {} with privileges :: {}", name, privileges);
        }
        log.info("Role {} created.", name);
        }
}
