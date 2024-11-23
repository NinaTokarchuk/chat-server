package com.masters.chat.filter;

import static com.masters.chat.constants.Constants.AUTHORIZATION_TOKEN_HEADER;
import static com.masters.chat.constants.Constants.BEARER;

import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExtractor implements Function<HttpServletRequest, Jwt> {

    private final JwtDecoder jwtDecoder;

    @Override
    public Jwt apply(final HttpServletRequest request) {
        try {
            return getBearerToken(request)
                    .map(jwtDecoder::decode)
                    .orElse(null);
        } catch (Exception ex) {
            log.error("Can't parse the JWT token: {}", ex.getMessage(), ex);
            return null;
        }
    }

    private Optional<String> getBearerToken(HttpServletRequest request) {
        var maybeToken = Optional.ofNullable(request)
                .map(req -> req.getHeader(AUTHORIZATION_TOKEN_HEADER))
                .map(header -> {
                    log.info("User's JWT: {}", header);
                    return header;
                });
        return maybeToken
                .filter(StringUtils::isNotBlank)
                .map(header -> StringUtils.replaceIgnoreCase(header, BEARER, Strings.EMPTY))
                .map(String::trim);
    }

}

