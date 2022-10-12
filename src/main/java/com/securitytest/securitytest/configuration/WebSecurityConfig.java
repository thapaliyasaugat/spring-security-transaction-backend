package com.securitytest.securitytest.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SetupRoleHierarchy setupRoleHierarchy;
    private final CustomUserDetailService customUserDetailService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    public WebSecurityConfig(CustomUserDetailService customUserDetailService, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.customUserDetailService = customUserDetailService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .expressionHandler(webExpressionHandler())
                .antMatchers("/api/auth/**").permitAll()
////               .antMatchers("/admin/**").hasAuthority(RoleName.ADMIN.name())
//                .antMatchers("api/admin/update/role/").hasAuthority(RoleName.ADMIN.name())
//                .antMatchers("/api/admin/role/").hasAuthority(RoleName.ADMIN.name())
//                .antMatchers("/api/user/email","/api/user/id").hasAnyAuthority(RoleName.TRANSACTION.name(),RoleName.ADMIN.name())
//                .antMatchers("/api/user/").hasAuthority(RoleName.ADMIN.name())
//                .antMatchers("/api/transaction/list","/api/transaction/time/between").hasAnyAuthority(RoleName.TRANSACTION.name(),RoleName.ADMIN.name())
//                .antMatchers("/api/transaction/create/","/api/transaction/code/","/api/transsaction/my_transactions").hasAnyAuthority(RoleName.TRANSACTION.name(),RoleName.ADMIN.name(),RoleName.CUSTOMER.name())
                .antMatchers("/api/user/me/detail").hasAnyAuthority("FETCH_OWN_DATA_PRIVILEGE")
                .antMatchers("/api/user/","/api/user/id/*","/api/user/email/*").hasAnyAuthority("FETCH_ALL_USERS_PRIVILEGE")
                .antMatchers("/api/user/load/balance").hasAnyAuthority("LOAD_BALANCE_PRIVILEGE")
                .antMatchers("/api/loadedbalancedetail/my").hasAnyAuthority("FETCH_OWN_LOADED_AMOUNT_DETAIL_PRIVILEGE")
                .antMatchers("/api/loadedbalancedetail/all","/api/loadedbalancedetail/email/*").hasAnyAuthority("FETCH_ALL_LOADED_AMOUNT_DETAIL_PRIVILEGE")
                .antMatchers("/api/transaction/create","/api/transaction/my_transactions").hasAnyAuthority("PERFORM_AND_VIEW_OWN_TRANSACTIONS_PRIVILEGE")
                .antMatchers("/api/transaction/list").hasAnyAuthority("VIEW_OTHERS_TRANSACTIONS_PRIVILEGE")
                .antMatchers("/api/admin/role/create","/api/admin/role/update/*").hasAuthority("CREATE_AND_UPDATE_NEW_ROLE_PRIVILEGE")
                .antMatchers("/api/admin/role/id/*","/api/admin/role/all","/api/admin/role/available-update/*").hasAnyAuthority("VIEW_USER_ROLES_PRIVILEGE")
                .antMatchers("/api/admin/block/*","/api/admin/activate/*").hasAnyAuthority("BLOCK_ACTIVATE_USER_PRIVILEGE")
                .antMatchers("/api/admin/update/role").hasAnyAuthority("UPDATE_USER_ROLE_PRIVILEGE")
                .antMatchers("/api/privilege/*").hasAnyAuthority("FETCH_PRIVILEGES_PRIVILEGE")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean(){
        return new JwtAuthenticationFilter();
    }

//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        String hierarchy = "ADMIN > CUSTOMER";
//        roleHierarchy.setHierarchy(hierarchy);
//        return roleHierarchy;
//    }

//    @Bean
//    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
//        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
//        expressionHandler.setRoleHierarchy(setupRoleHierarchy.roleHierarchy());
//        return expressionHandler;
//    }

    private SecurityExpressionHandler<FilterInvocation>    webExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler     = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(setupRoleHierarchy.roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }

}
