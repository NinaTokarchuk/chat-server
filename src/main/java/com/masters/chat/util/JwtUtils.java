package com.masters.chat.util;

import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class JwtUtils {

    public static Authentication createAuth(Jwt jwt) {
        var email = getClaimAttribute(jwt, "email");
        var authorities = getClaimAttribute(jwt, "authorities");
        var grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
        return new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities);
    }

    public static String getClaimAttribute(Jwt jwt, String attribute) {
        return Optional.ofNullable(jwt.getClaimAsString(attribute))
                .orElseGet(() -> {
                    log.warn("Claim {} is not present, check the system or user setup, jwt: {}", attribute, jwt);
                    return Strings.EMPTY;
                });
    }

}
