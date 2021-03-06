package com.app.e_library.security;

import com.app.e_library.security.filter.AuthenticationFilter;
import com.app.e_library.security.filter.AuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableGlobalMethodSecurity(
        jsr250Enabled = true,
        securedEnabled = true,
        prePostEnabled = true
)
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        AuthenticationFilter authenticationFilter = AuthenticationFilter
                .builder()
                .authenticationManager(authenticationManager())
                .jwtUtil(jwtUtil)
                .build();

        authenticationFilter.setFilterProcessesUrl("/login");

        http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login/**", "/register/**", "/security/token/refresh/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(authenticationFilter)
                .addFilterBefore(
                        AuthorizationFilter.builder().jwtUtil(jwtUtil).build(),
                        UsernamePasswordAuthenticationFilter.class
                );
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

}