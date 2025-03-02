package com.nottie.mapper;

import com.nottie.dto.request.auth.CreateUserDTO;
import com.nottie.dto.response.auth.CreatedUserDTO;
import com.nottie.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //@Mapping(target = "confirmPassword", ignore = true)
    User createUserDTOToUser(CreateUserDTO createUserDTO);
    CreatedUserDTO userToCreatedUserDTO(User user);

}
