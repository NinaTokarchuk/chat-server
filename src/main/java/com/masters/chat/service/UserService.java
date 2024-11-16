package com.masters.chat.service;

import java.util.List;

import com.masters.chat.domain.User;
import com.masters.chat.dto.UpdateUserDto;
import com.masters.chat.dto.UserDto;

public interface UserService {

    UserDto findUserById(Integer id);

    UserDto findUserByProfile(String jwt);

    UserDto updateUser(String token, UpdateUserDto updateUserDto);

    List<UserDto> searchUsers(String query);

}
