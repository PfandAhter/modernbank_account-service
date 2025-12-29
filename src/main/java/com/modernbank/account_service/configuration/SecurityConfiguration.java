package com.modernbank.account_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${security.encryption.secret-key}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF genelde mikroservislerde ve API'lerde kapatılır
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Actuator endpointlerine (Prometheus dahil) herkes erişebilsin
                        .requestMatchers("/actuator/**").permitAll()

                        // Diğer tüm isteklere de izin ver (Çünkü güvenliği Gateway'de yapıyorsun)
                        // Eğer bu serviste özel güvenlik istiyorsan burayı değiştirebilirsin.
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public Pbkdf2PasswordEncoder passwordEncoder() {
        // parametreler: secret, iteration, hash width (bit)
        return new Pbkdf2PasswordEncoder(
                secretKey,                           // secret
                16,                                       // salt length (bytes)
                240000,                                   // iterations
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256 // algorithm (enum)
        );
    }
}