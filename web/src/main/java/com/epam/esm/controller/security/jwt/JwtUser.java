package com.epam.esm.controller.security.jwt;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * JwtUser
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@RequiredArgsConstructor
@Data
public class JwtUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final boolean enabled = true;
    private final boolean isAccountNonExpired = true;
    private final boolean isAccountNonLocked = true;
    private final boolean isCredentialsNonExpired = true;
    private Date lastPasswordResetDate;
    private final Collection<? extends GrantedAuthority> authorities;
}
