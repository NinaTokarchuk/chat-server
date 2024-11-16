package com.masters.chat.service.impl;

import static com.masters.chat.util.JwtUtils.getClaimAttribute;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.masters.chat.service.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenProviderImpl implements TokenProvider {

    private static final String EMAIL = "email";

    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;

    @Override
    public String generateToken(Authentication authentication) {
        var claims = JwtClaimsSet.builder()
                .issuer("Nina")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .claim("email", authentication.getName())
                .build();
        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return jwtEncoder.encode(encoderParameters).getTokenValue();
    }

    @Override
    public String getEmailFromToken(String token) {
        token = token.substring(7);
        var jwt = jwtDecoder.decode(token);
        return Optional.ofNullable(getClaimAttribute(jwt, EMAIL)).orElseThrow(() -> new BadCredentialsException("Invalid token received"));
    }

}

