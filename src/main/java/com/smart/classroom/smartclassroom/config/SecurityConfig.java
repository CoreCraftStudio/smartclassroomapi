package com.smart.classroom.smartclassroom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.smart.classroom.smartclassroom.util.Constant.UserConstant.STUDENT;
import static com.smart.classroom.smartclassroom.util.Constant.UserConstant.TEACHER;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChainProd(AuthFilter authFilter, HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(HttpMethod.POST, "/user").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth").permitAll()

                                .requestMatchers("/classroom/**").hasRole(TEACHER)

                                .requestMatchers(HttpMethod.PUT, "/quiz").hasRole(STUDENT)
                                .requestMatchers(HttpMethod.POST, "/quiz").hasRole(TEACHER)
                                .requestMatchers(HttpMethod.DELETE, "/quiz/**").hasRole(TEACHER)

                                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
