package com.masters.chat.controller;

import static com.masters.chat.constants.Constants.AUTHORIZATION_TOKEN_HEADER;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.masters.chat.dto.UpdateUserDto;
import com.masters.chat.dto.UserDto;
import com.masters.chat.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public UserDto getUserProfile(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token) {
        return userService.findUserByProfile(token);
    }

    @GetMapping
    public List<UserDto> searchUsers(@RequestParam(required = false) String query) {
        return userService.searchUsers(query);
    }

    @PutMapping("/update/{id}")
    public UserDto updateUser(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token,
                              @PathVariable Integer id,
                              @RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(token, updateUserDto);
    }

}
