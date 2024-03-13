package com.iablonski.websecfluxapp.mapper;

import com.iablonski.websecfluxapp.dto.UserDTO;
import com.iablonski.websecfluxapp.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);
    @InheritInverseConfiguration
    User toUser(UserDTO userDTO);
}
