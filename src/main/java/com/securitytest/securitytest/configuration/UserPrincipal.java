package com.securitytest.securitytest.configuration;

import com.securitytest.securitytest.models.Privilege;
import com.securitytest.securitytest.models.Role;
import com.securitytest.securitytest.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UserPrincipal implements UserDetails {
    private int id;
    private String userName;
    private String email;
    private String password;
    private Set<Role> roles;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(int id, String userName, String email, String password, Set<Role> roles, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = getPrivileges(user.getRoles()).stream().map(privilege ->
                new SimpleGrantedAuthority(privilege.getName())
        ).collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles(),
                authorities
        );
    }
    private static List<Privilege> getPrivileges(Set<Role> roles){
        List<Privilege> privileges = new ArrayList<>();
        roles.forEach(role -> privileges.addAll(role.getPrivileges()));
        return privileges;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
