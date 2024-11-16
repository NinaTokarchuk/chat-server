package com.masters.chat.service.impl;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;

import com.masters.chat.dto.UpdateUserDto;
import com.masters.chat.dto.UserDto;
import com.masters.chat.exception.UserNotFoundException;
import com.masters.chat.mapper.UserMapper;
import com.masters.chat.repository.UserRepository;
import com.masters.chat.service.TokenProvider;
import com.masters.chat.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final UserMapper userMapper;

    @Override
    public UserDto findUserById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + id));
    }

    @Override
    public UserDto findUserByProfile(String jwt) {
        var email = tokenProvider.getEmailFromToken(jwt);
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
    }

    @Override
    public UserDto updateUser(String token, UpdateUserDto updateUserDto) {
        var email = tokenProvider.getEmailFromToken(token);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));

        if (updateUserDto.getFullName() != null) {
            user.setFullName(updateUserDto.getFullName());
        }

        if (updateUserDto.getProfilePicture() != null) {
            user.setProfilePicture(updateUserDto.getProfilePicture());
        }

        userRepository.save(user);
        return userMapper.toDto(user);
    }


    @Override
    public List<UserDto> searchUsers(String query) {
        return userRepository.searchUsers(query)
                .stream()
                .map(userMapper::toDto)
                .collect(toList());
    }

}
