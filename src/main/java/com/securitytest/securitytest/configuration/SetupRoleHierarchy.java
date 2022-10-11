package com.securitytest.securitytest.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
@Slf4j
public class SetupRoleHierarchy {
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        log.info("checking role hierarchy...");
//        String hierarchy = "ROLE_ADMIN  >  ROLE_ADMIN-SUPPORT \n ROLE_ADMIN-SUPPORT  >  ROLE_TRANSACTION \n ROLE_TRANSACTION  >  ROLE_CUSTOMER";
        String hierarchy = "ROLE_ADMIN > ROLE_ADMIN-SUPPORT \n ROLE_ADMIN-SUPPORT > ROLE_TRANSACTION \n ROLE_TRANSACTION > ROLE_CUSTOMER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

//    @Bean
//    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
//        log.info("web Security expression handler...");
//        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
//        expressionHandler.setRoleHierarchy(roleHierarchy());
//        return expressionHandler;
//    }
}
