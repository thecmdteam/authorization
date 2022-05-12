package com.cmd.authorization.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
public class User implements UserDetails{
    private String username;

    private String password;
    private boolean enabled = false;

    private boolean credentialsNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    private List<GrantedAuthority> authorities = new ArrayList<>(List.of(new SimpleGrantedAuthority("USER")));

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
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
