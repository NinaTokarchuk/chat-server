package com.masters.chat.config;

import static com.masters.chat.constants.Constants.AUTHORIZATION_TOKEN_HEADER;
import static com.masters.chat.constants.Constants.SECRET_KEY;
import static java.util.Collections.singletonList;

import java.util.Base64;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.masters.chat.filter.JwtFilter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppConfig {

    private static final String ALLOWED_ORIGINS = "*";
    private static final long MAX_AGE = 3600L;
    private static final String HMACSHA_256 = "HMACSHA256";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/**")
                        .authenticated().anyRequest().permitAll())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .cors().configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.addAllowedOriginPattern(ALLOWED_ORIGINS);
                    corsConfiguration.setAllowedMethods(singletonList(ALLOWED_ORIGINS));
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setAllowedHeaders(singletonList(ALLOWED_ORIGINS));
                    corsConfiguration.setExposedHeaders(List.of(AUTHORIZATION_TOKEN_HEADER));
                    corsConfiguration.setMaxAge(MAX_AGE);

                    return corsConfiguration;
                }).and().formLogin().and().httpBasic();

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, HMACSHA_256);

        return NimbusJwtDecoder.withSecretKey(keySpec).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(Base64.getDecoder().decode(SECRET_KEY)));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
