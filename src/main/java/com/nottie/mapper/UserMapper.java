package com.nottie.mapper;

import com.nottie.dto.request.auth.CreateUserDTO;
import com.nottie.dto.response.auth.CreatedUserDTO;
import com.nottie.dto.response.auth.UserLoggedDTO;
import com.nottie.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //@Mapping(target = "confirmPassword", ignore = true)
    User createUserDTOToUser(CreateUserDTO createUserDTO);

    // Register User
    CreatedUserDTO userToCreatedUserDTO(User user);

    // Login User
    UserLoggedDTO userToUserLoggedDTO(User user);

}
