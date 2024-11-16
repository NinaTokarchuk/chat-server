package com.masters.chat.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.masters.chat.domain.User;
import com.masters.chat.dto.UserDto;

@Mapper(config = CommonMapperConfig.class)
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    User fromDto(UserDto userDto);

    List<UserDto> toDtos(List<User> users);

    List<User> fromDtos(List<UserDto> userDtos);

}
