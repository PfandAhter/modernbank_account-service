package com.modernbank.account_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
public class SecurityConfiguration {

    @Value("${security.encryption.secret-key}")
    private String secretKey;

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