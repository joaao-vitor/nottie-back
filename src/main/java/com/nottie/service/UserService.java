package com.nottie.service;

import com.nottie.dto.request.user.UserSummaryDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.mapper.UserMapper;
import com.nottie.model.User;
import com.nottie.model.Workstation;
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

        User userFollowed = userRepository.getUserById(followId).orElseThrow(
                () -> new BadRequestException("User not found")
        );

        if(userAuthenticated.getFollowingUsers().contains(userFollowed))
            throw new BadRequestException("You're already following this user");

        userAuthenticated.getFollowingUsers().add(userFollowed);
        userRepository.save(userAuthenticated);
    }


    private void unfollowUser(Long followId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if(userAuthenticated.getId().equals(followId))
            throw new BadRequestException("You can not unfollow yourself");

        User userFollowed = userRepository.getUserById(followId).orElseThrow(
                () -> new BadRequestException("User not found")
        );

        if(!userAuthenticated.getFollowingUsers().contains(userFollowed))
            throw new BadRequestException("You're not following this user");

        userAuthenticated.getFollowingUsers().remove(userFollowed);
        userRepository.save(userAuthenticated);
    }

    private void followWorkstation(Long workstationId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        Workstation workstation = workstationRepository.getWorkstationsById(workstationId)
                .orElseThrow(() -> new BadRequestException("Workstation not found"));

        if(userAuthenticated.getFollowingWorkstations().contains(workstation))
            throw new BadRequestException("You're already following this workstation");

        userAuthenticated.getFollowingWorkstations().add(workstation);
        userRepository.save(userAuthenticated);
    }

    private void unfollowWorkstation(Long workstationId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        Workstation workstation = workstationRepository.getWorkstationsById(workstationId)
                .orElseThrow(() -> new BadRequestException("Workstation not found"));

        if(!userAuthenticated.getFollowingWorkstations().contains(workstation))
            throw new BadRequestException("You're not following this workstation");

        userAuthenticated.getFollowingWorkstations().remove(workstation);
        userRepository.save(userAuthenticated);
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
