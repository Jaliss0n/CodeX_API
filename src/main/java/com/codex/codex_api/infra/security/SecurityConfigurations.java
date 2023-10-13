package com.codex.codex_api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() //Fechar para apenas adm
                        .requestMatchers(HttpMethod.POST, "/auth/register/unity").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/access/adm").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/access/adm/list").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/access/adm").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/access/adm").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/access/unity").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/access/unity").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/access/unity").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/avatar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/avatar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/my/avatar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/my/avatar").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
