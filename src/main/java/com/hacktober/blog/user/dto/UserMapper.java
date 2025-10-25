package com.hacktober.blog.user.dto;

import com.hacktober.blog.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromDtoToEntity(UserDto userDto);
    UserDto fromEntityToDto(User user);
}
