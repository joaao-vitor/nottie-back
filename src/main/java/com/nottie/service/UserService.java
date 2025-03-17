package com.nottie.service;

import com.nottie.dto.request.user.EditUserDTO;
import com.nottie.dto.request.user.UserSummaryDTO;
import com.nottie.dto.response.user.EditedUserDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.exception.UnauthorizedException;
import com.nottie.mapper.UserMapper;
import com.nottie.model.User;
import com.nottie.repository.UserRepository;
import com.nottie.repository.WorkstationRepository;
import com.nottie.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final WorkstationRepository workstationRepository;

    public enum FollowType { USER, WORKSTATION }

    public UserService(UserRepository userRepository, AuthUtil authUtil, WorkstationRepository workstationRepository) {
        this.userRepository = userRepository;
        this.authUtil = authUtil;
        this.workstationRepository = workstationRepository;
    }

    @Transactional
    public EditedUserDTO updateUser(Long id, EditUserDTO editUserDTO) {
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if (!userAuthenticated.getId().equals(id))
            throw new UnauthorizedException("You are not authorized to update this User");

        if (userRepository.existsByUsername(editUserDTO.username()) && !userAuthenticated.getUsername().equals(editUserDTO.username()))
            throw new BadRequestException("Username already exists");

        userAuthenticated.setUsername(editUserDTO.username());
        userAuthenticated.setName(editUserDTO.name());
        userRepository.save(userAuthenticated);

        return UserMapper.INSTANCE.userToEditedUserDTO(userAuthenticated);
    }

    @Transactional
    public void follow(Long id, FollowType type) {
        if(type.equals(FollowType.USER)) {
            followUser(id);
        } else if(type.equals(FollowType.WORKSTATION)) {
            followWorkstation(id);
        }
    }

    @Transactional
    public void unfollow(Long id, FollowType type) {
        if(type.equals(FollowType.USER)) {
            unfollowUser(id);
        } else if(type.equals(FollowType.WORKSTATION)) {
            unfollowWorkstation(id);
        }
    }

    private void followUser(Long followId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if(userAuthenticated.getId().equals(followId))
            throw new BadRequestException("You can not follow yourself");

        if(!userRepository.existsById(followId))
            throw new NotFoundException("User not found");

        System.out.println(userRepository.existsByIdAndFollowingUsers_Id(userAuthenticated.getId(), followId));
        if(userRepository.existsByIdAndFollowingUsers_Id(userAuthenticated.getId(), followId))
            throw new BadRequestException("You're already following this user");

        userRepository.followUser(userAuthenticated.getId(), followId);
    }


    private void unfollowUser(Long unfollowId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if(userAuthenticated.getId().equals(unfollowId))
            throw new BadRequestException("You can not unfollow yourself");


        if(!userRepository.existsById(unfollowId))
            throw new NotFoundException("User not found");

        if(!userRepository.existsByIdAndFollowingUsers_Id(userAuthenticated.getId(), unfollowId))
            throw new BadRequestException("You're not following this user");

        userRepository.unfollowUser(userAuthenticated.getId(), unfollowId);
    }

    private void followWorkstation(Long workstationId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Workstation not found");

        if(userRepository.existsByIdAndFollowingWorkstations_Id(userAuthenticated.getId(), workstationId))
            throw new NotFoundException("You're already following workstation");

        userRepository.followWorkstation(userAuthenticated.getId(), workstationId);
    }

    private void unfollowWorkstation(Long workstationId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Workstation not found");

        if(!userRepository.existsByIdAndFollowingWorkstations_Id(userAuthenticated.getId(), workstationId))
            throw new NotFoundException("You're not following workstation");

        userRepository.unfollowWorkstation(userAuthenticated.getId(), workstationId);
    }

    public UserSummaryDTO getUserSummary(Long id) {
        User user = userRepository.getUserById(id).orElseThrow(
                () -> new BadRequestException("User not found")
        );

        UserSummaryDTO userSummaryDTO = UserMapper.INSTANCE.userToUserSummaryDTO(user);

        Long followersUserCount = userRepository.countFollowersUsersByUserId(user.getId()).orElseThrow();
        Long followersWorkstationCount = userRepository.countFollowersWorkstationsByUserId(user.getId()).orElseThrow();
        userSummaryDTO.setFollowersCount(followersUserCount + followersWorkstationCount);

        Long followingUserCount = userRepository.countFollowingUsersByUserId(user.getId()).orElseThrow();
        Long followingWorkstationCount = userRepository.countFollowingWorkstationsByUserId(user.getId()).orElseThrow();

        userSummaryDTO.setFollowingCount(followingUserCount + followingWorkstationCount);
        return userSummaryDTO;
    }
}
