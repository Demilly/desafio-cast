package br.com.integration.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("CLIENT")
                        .password("password")
                        .roles("CLIENT")
                        .build(),
                User.withUsername("ADMIN")
                        .password("{noop}password")
                        .roles("ADMIN")
                        .build()
        );
    }
}
