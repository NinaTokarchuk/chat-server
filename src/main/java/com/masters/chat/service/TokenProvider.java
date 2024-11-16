package com.masters.chat.service;

import org.springframework.security.core.Authentication;

public interface TokenProvider {

    String generateToken(Authentication authentication);

    String getEmailFromToken(String token);

}
