package com.nottie.mapper;

import com.nottie.dto.request.auth.CreateUserDTO;
import com.nottie.dto.response.note.NoteAuthorDTO;
import com.nottie.dto.response.user.AuthenticatedUserDTO;
import com.nottie.dto.response.user.SearchUserDTO;
import com.nottie.dto.response.user.SummaryDTO;
import com.nottie.dto.request.workstation.WorkstationLeaderDTO;
import com.nottie.dto.response.auth.CreatedUserDTO;
import com.nottie.dto.response.auth.UserLoggedDTO;
import com.nottie.dto.response.user.EditedUserDTO;
import com.nottie.dto.response.workstation.WorkstationMemberDTO;
import com.nottie.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //@Mapping(target = "confirmPassword", ignore = true)
    User createUserDTOToUser(CreateUserDTO createUserDTO);

    // Register User
    CreatedUserDTO userToCreatedUserDTO(User user);

    // Login User
    UserLoggedDTO userToUserLoggedDTO(User user);

    // User Summary
    @Mapping(target = "followingCount", ignore = true)
    @Mapping(target = "followersCount", ignore = true)
    SummaryDTO userToSummaryDTO(User user);

    // Get Leaders (workstation)
    List<WorkstationLeaderDTO> userListToWorkstationLeaderDTOList(List<User> users);

    // Update User
    EditedUserDTO userToEditedUserDTO(User userAuthenticated);

    AuthenticatedUserDTO userToAuthenticatedUserDTO(User userAuthenticated);

    WorkstationMemberDTO userToWorkstationMemberDTO(User creator);

    List<SearchUserDTO> usersToSearchUserDTOS(List<User> users);
    Set<SearchUserDTO> usersToSearchUserDTOS(Set<User> users);

    Set<NoteAuthorDTO> usersToNoteAuthorDTOS(Set<User> users);
    NoteAuthorDTO userToNoteAuthorDTO(User user);
}
