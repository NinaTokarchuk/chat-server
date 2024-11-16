package com.masters.chat.controller;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masters.chat.domain.User;
import com.masters.chat.dto.AuthResponseDto;
import com.masters.chat.dto.LoginRequestDto;
import com.masters.chat.exception.UserExistsException;
import com.masters.chat.repository.UserRepository;
import com.masters.chat.service.TokenProvider;
import com.masters.chat.service.impl.CustomUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final CustomUserService customUserService;

    @PostMapping("/signup")
    public AuthResponseDto createUser(@RequestBody User user) {
        var email = user.getEmail();

        var foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent()) {
            throw new UserExistsException("User already exists with email " + email);
        }

        var fullName = user.getFullName();
        var password = user.getPassword();

        var createdUser = User.builder()
                .email(email)
                .fullName(fullName)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(createdUser);

        var authentication = new UsernamePasswordAuthenticationToken(email, createdUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = tokenProvider.generateToken(authentication);
        return AuthResponseDto.builder()
                .jwt(jwt)
                .isAuth(true)
                .build();
    }

    @PostMapping("/signin")
    public AuthResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        var email = loginRequestDto.getEmail();
        var password = loginRequestDto.getPassword();

        var authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = tokenProvider.generateToken(authentication);
        return AuthResponseDto.builder()
                .jwt(jwt)
                .isAuth(true)
                .build();
    }

    public Authentication authenticate(String username, String password) {
        var userDetails = customUserService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password or username");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
