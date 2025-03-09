package com.nottie.service;

import com.nottie.dto.request.user.UserSummaryDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.mapper.UserMapper;
import com.nottie.model.User;
import com.nottie.repository.UserRepository;
import com.nottie.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    public UserService(UserRepository userRepository, AuthUtil authUtil) {
        this.userRepository = userRepository;
        this.authUtil = authUtil;
    }

    @Transactional
    public void followUser(Long followId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if(userAuthenticated.getId().equals(followId))
            throw new BadRequestException("You can not follow yourself");

        User userFollowed = userRepository.getUserById(followId).orElseThrow(
                () -> new BadRequestException("User not found")
        );

        if(userAuthenticated.getFollowing().contains(userFollowed))
            throw new BadRequestException("You're already following this user");

        userAuthenticated.getFollowing().add(userFollowed);
        userRepository.save(userAuthenticated);
    }


    @Transactional
    public void unfollowUser(Long followId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if(userAuthenticated.getId().equals(followId))
            throw new BadRequestException("You can not unfollow yourself");

        User userFollowed = userRepository.getUserById(followId).orElseThrow(
                () -> new BadRequestException("User not found")
        );

        if(!userAuthenticated.getFollowing().contains(userFollowed))
            throw new BadRequestException("You're not following this user");

        userAuthenticated.getFollowing().remove(userFollowed);
        userRepository.save(userAuthenticated);
    }

    public UserSummaryDTO getUserSummary(Long id) {
        User user = userRepository.getUserById(id).orElseThrow(
                () -> new BadRequestException("User not found")
        );

        UserSummaryDTO userSummaryDTO = UserMapper.INSTANCE.userToUserSummaryDTO(user);

        Long followersCount = userRepository.countFollowersByUserId(user.getId()).orElseThrow();
        userSummaryDTO.setFollowersCount(followersCount);

        Long followingCount = userRepository.countFollwingByUserId(user.getId()).orElseThrow();
        userSummaryDTO.setFollowingCount(followingCount);

        return userSummaryDTO;
    }
}
