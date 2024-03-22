package com.example.SpringHomeWork7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Класс, реализующий методы аутентификации пользователей и определяющий алгоритм работы фильтра
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Метод определяющий работу фильтра в зависимости от типа пользователей:
     * USER - получает доступ к странице с открытыми данными;
     * ADMIN - получает доступ к странице с закрытой информацией
     * @param http - запрос пользователя
     * @return - возвращает страницу сайта, соответствующая типу пользователя
     * @throws Exception
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/css/**", "/favicon.ico", "/", "/index").permitAll()
                .requestMatchers("/public-data").hasAnyRole("USER")
                .requestMatchers("/private-data").hasAnyRole("ADMIN")
                .anyRequest().authenticated())
        .formLogin(login -> login.defaultSuccessUrl("/")
                .permitAll())
        .logout(logout -> logout
                .logoutSuccessUrl("/"));
        return http.build();
    }

    /**
     * Метод шифрования паролей пользователей
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Метод предоставления доступа пользователям к информации в соответствии с их ролью в системе
     * @return возвращает информацию о пользователе и его правах доступа
     */
    @Bean
    UserDetailsManager inMemoryUserDetailsManager() {
        var user1 = User.withUsername("user").password("{noop}123").roles("USER").build();
        var user2 = User.withUsername("admin").password("{noop}qwe").roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(user1, user2);
    }
}
