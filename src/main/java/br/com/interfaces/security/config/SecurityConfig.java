package br.com.interfaces.security.config;

import br.com.interfaces.security.CustomLoginSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
public class SecurityConfig {

    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder encoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .inMemoryAuthentication()
                .withUser("admin").password(encoder.encode("admin123")).roles("ADMIN")
                .and()
                .withUser("cliente").password(encoder.encode("cliente123")).roles("CLIENT")
                .and()
                .and().build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/client/api/test/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/client/**").hasRole("CLIENT")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")   // <- aqui
                .successHandler(customLoginSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**", "/client/api/test/**").and().headers().frameOptions().sameOrigin();

        return http.build();
    }
}
