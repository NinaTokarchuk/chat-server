package com.masters.chat.util;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class JwtUtils {
    public static final String BEARER = "Bearer";

    public static final String EMAIL = "email";

    private static final String USER_ID = "user_id";
    private static final String FIREBASE_CLAIM = "firebase";
    private static final String TENANT_CLAIM = "tenant";
    private static final Configuration JSONPATH_CONFIG = Configuration.defaultConfiguration()
            .addOptions(Option.SUPPRESS_EXCEPTIONS);

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

    private static List<String> toList(Object roles) {
        if (roles instanceof String) {
            return List.of((String) roles);
        } else if (roles instanceof List) {
            return (List<String>) roles;
        } else {
            return emptyList();
        }
    }

}
