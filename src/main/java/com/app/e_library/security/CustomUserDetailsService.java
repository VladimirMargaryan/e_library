package com.app.e_library.security;


import com.app.e_library.service.UserService;
import com.app.e_library.service.dto.UserDto;
import com.app.e_library.service.dto.UserStatusType;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component("userDetailsService")
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        UserDto user = userService.getByEmail(email);
        if (user != null && !user.getStatus().equals(UserStatusType.UNVERIFIED)) {
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getRollName()));
            return new org.springframework.security.core.userdetails
                    .User(user.getEmail(), user.getPassword(), grantedAuthorities);
        } else
            throw new UsernameNotFoundException("Username not found!");
    }
}