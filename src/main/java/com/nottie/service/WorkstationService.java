package com.nottie.service;

import com.nottie.dto.request.workstation.CreateWorkstationDTO;
import com.nottie.dto.response.workstation.CreatedWorkstationDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.mapper.WorkstationMapper;
import com.nottie.model.User;
import com.nottie.model.Workstation;
import com.nottie.repository.UserRepository;
import com.nottie.repository.WorkstationRepository;
import com.nottie.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class WorkstationService {
    private final WorkstationRepository workstationRepository;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;


    public enum FollowType { USER, WORKSTATION }

    public WorkstationService(WorkstationRepository workstationRepository, AuthUtil authUtil, UserRepository userRepository) {
        this.workstationRepository = workstationRepository;
        this.authUtil = authUtil;
        this.userRepository = userRepository;
    }

    @Transactional
    public void follow(Long workstationId, Long followId, FollowType followType) {
        if (followType == FollowType.USER) {
            followUser(workstationId, followId);
        } else if (followType == FollowType.WORKSTATION) {
            followWorkstation(workstationId, followId);
        }
    }

    @Transactional
    public void unfollow(Long workstationId, Long unfollowId, FollowType followType) {
        if (followType == FollowType.USER) {
            unfollowUser(workstationId, unfollowId);
        } else if (followType == FollowType.WORKSTATION) {
            unfollowWorkstation(workstationId, unfollowId);
        }
    }

    private void followWorkstation(Long workstationId, Long followId) {
        if(workstationId.equals(followId))
            throw new BadRequestException("You can't follow the same workstation");

        Workstation workstation = workstationRepository.findById(workstationId)
                .orElseThrow(() -> new NotFoundException("Workstation not found"));

        Workstation followingWorkstation = workstationRepository.findById(followId)
                .orElseThrow(() -> new NotFoundException("Following workstation not found"));

        if(workstationRepository.existsByIdAndFollowingWorkstations_Id(workstationId, followId))
            throw new BadRequestException("You're already following this workstation");

        workstation.getFollowingWorkstations().add(followingWorkstation);
        workstationRepository.save(workstation);
    }

    private void followUser(Long workstationId, Long userId) {
        Workstation workstation = workstationRepository.findById(workstationId)
                .orElseThrow(() -> new NotFoundException("Workstation not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (workstationRepository.existsByIdAndFollowingUsers_Id(workstationId, userId)) {
            throw new BadRequestException("You're already following this user");
        }

        workstation.getFollowingUsers().add(user);
        workstationRepository.save(workstation);
    }

    private void unfollowUser(Long workstationId, Long userId) {
        Workstation workstation = workstationRepository.findById(workstationId)
                .orElseThrow(() -> new NotFoundException("Workstation not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!workstationRepository.existsByIdAndFollowingUsers_Id(workstationId, userId)) {
            throw new BadRequestException("You're not following this user");
        }

        workstation.getFollowingUsers().remove(user);
        workstationRepository.save(workstation);
    }

    private void unfollowWorkstation(Long workstationId, Long unfollowId) {
        if(workstationId.equals(unfollowId))
            throw new BadRequestException("You can't unfollow the same workstation");

        Workstation workstation = workstationRepository.findById(workstationId)
                .orElseThrow(() -> new NotFoundException("Workstation not found"));

        Workstation followingWorkstation = workstationRepository.findById(unfollowId)
                .orElseThrow(() -> new NotFoundException("Following workstation not found"));

        if(!workstationRepository.existsByIdAndFollowingWorkstations_Id(workstationId, unfollowId))
            throw new BadRequestException("You're not following this workstation");

        workstation.getFollowingWorkstations().remove(followingWorkstation);
        workstationRepository.save(workstation);
    }

    @Transactional
    public CreatedWorkstationDTO createWorkstation(CreateWorkstationDTO createWorkstationDTO) {
        if(createWorkstationDTO.name() == null || createWorkstationDTO.name().isEmpty())
            throw new BadRequestException("Name cannot be empty");

        if(createWorkstationDTO.username() == null || createWorkstationDTO.username().isEmpty())
            throw new BadRequestException("Username cannot be empty");

        if(userRepository.getUserByUsername(createWorkstationDTO.username()).isPresent()
        || workstationRepository.getWorkstationsByUsername(createWorkstationDTO.username()).isPresent())
            throw new BadRequestException("Username already exists");

        Workstation workstation = WorkstationMapper.INSTANCE.createWorkstationDTOToWorkstation(createWorkstationDTO);

        User authenticatedUser = authUtil.getAuthenticatedUser();

        workstation.getLeaders().add(authenticatedUser);
        workstation.getMembers().add(authenticatedUser);

        workstationRepository.save(workstation);

        return WorkstationMapper.INSTANCE.workstationToCreatedWorkstationDTO(workstation);
    }

    public boolean isLeader(Long workstationId) {
        User authenticatedUser = authUtil.getAuthenticatedUser();

        return workstationRepository.existsByIdAndLeaders_Id(workstationId, authenticatedUser.getId());
    }

}
