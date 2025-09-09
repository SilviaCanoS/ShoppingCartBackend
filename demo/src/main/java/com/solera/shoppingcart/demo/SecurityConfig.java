package com.solera.shoppingcart.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
            .password(passwordEncoder.encode("user"))
            .roles("USER")
            .build();
 
        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder.encode("admin"))
            .roles("ADMIN", "USER") // Admin also has USER role
            .build();
 
        return new InMemoryUserDetailsManager(user, admin);
    }
 
    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF protection for simplicity, not recommended for production
            
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/v1/**").hasRole("ADMIN") // Solo usuarios con el rol ADMIN pueden acceder a rutas que empiecen con /API/v1/.
                //.requestMatchers("/api/v1/product").hasAnyRole("USER", "ADMIN") //Usuarios con rol USER o ADMIN pueden acceder a rutas que empiecen con /user/.
                .anyRequest().authenticated() // All other requests require authentication
            )
            .formLogin(form -> form.permitAll()) // Enable login by form
            .httpBasic(basic -> basic.init(http)); // Enable HTTP Basic authentication
 
        return http.build();
    }*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF protection for simplicity, not recommended for production
            .authorizeHttpRequests(authorize ->
                authorize.requestMatchers(HttpMethod.GET, "/api/v1/product").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/api/v1/product/*").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/v1/cart").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/api/v1/cart/*").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/cart/*").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/cart/*").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/v1/relation").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/api/v1/relation/cart/*").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/relation/cart/*").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/relation/one").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/api/v1/relation/**").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/relation/**").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/relation/**").hasRole("USER")
                .requestMatchers("/api/v1/****").hasRole("ADMIN")
                .anyRequest().authenticated()) // All other requests require authentication
                .formLogin(form -> form.permitAll()) // Enable login by form
                .httpBasic(basic -> basic.init(http)); // Enable HTTP Basic authentication
 
        return http.build();
    }
}
