package co.uk.negura.workshop_customer_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/v1/customer").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/customer/{ID}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/customer").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/customer").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}
