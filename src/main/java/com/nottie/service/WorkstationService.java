package com.nottie.service;

import com.nottie.dto.response.user.SummaryDTO;
import com.nottie.dto.request.workstation.CreateWorkstationDTO;
import com.nottie.dto.request.workstation.EditWorkstationDTO;
import com.nottie.dto.request.workstation.GetLeadersDTO;
import com.nottie.dto.request.workstation.WorkstationLeaderDTO;
import com.nottie.dto.response.workstation.*;
import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.mapper.UserMapper;
import com.nottie.mapper.WorkstationMapper;
import com.nottie.model.User;
import com.nottie.model.Workstation;
import com.nottie.repository.UserRepository;
import com.nottie.repository.WorkstationRepository;
import com.nottie.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class WorkstationService {
    private final WorkstationRepository workstationRepository;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;



    public enum FollowType {USER, WORKSTATION}

    public WorkstationService(WorkstationRepository workstationRepository, AuthUtil authUtil, UserRepository userRepository, CloudinaryService cloudinaryService) {
        this.workstationRepository = workstationRepository;
        this.authUtil = authUtil;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public SummaryDTO getWorkstationSummary(Long workstationId) {

        Workstation workstation = workstationRepository.findById(workstationId).orElseThrow(
                () -> new BadRequestException("Workstation not found")
        );

        SummaryDTO summaryDTO = WorkstationMapper.INSTANCE.workstationToSummaryDTO(workstation);

        Long followersUserCount = workstationRepository.countFollowersUsersByWorkstationId(workstation.getId()).orElseThrow();
        Long followersWorkstationCount = workstationRepository.countFollowersWorkstationsByWorkstationId(workstation.getId()).orElseThrow();
        summaryDTO.setFollowersCount(followersUserCount + followersWorkstationCount);

        Long followingUserCount = workstationRepository.countFollowingUsersByWorkstationId(workstation.getId()).orElseThrow();
        Long followingWorkstationCount = workstationRepository.countFollowingWorkstationsByWorkstationId(workstation.getId()).orElseThrow();

        summaryDTO.setFollowingCount(followingUserCount + followingWorkstationCount);
        return summaryDTO;
    }

    @Transactional
    public CreatedWorkstationDTO createWorkstation(CreateWorkstationDTO createWorkstationDTO) {
        if (createWorkstationDTO.name() == null || createWorkstationDTO.name().isEmpty())
            throw new BadRequestException("Name cannot be empty");

        if (createWorkstationDTO.username() == null || createWorkstationDTO.username().isEmpty())
            throw new BadRequestException("Username cannot be empty");

        if (userRepository.getUserByUsername(createWorkstationDTO.username()).isPresent()
                || workstationRepository.getWorkstationsByUsername(createWorkstationDTO.username()).isPresent())
            throw new BadRequestException("Username already exists");

        Workstation workstation = WorkstationMapper.INSTANCE.createWorkstationDTOToWorkstation(createWorkstationDTO);

        User authenticatedUser = authUtil.getAuthenticatedUser();

        workstation.getLeaders().add(authenticatedUser);
        workstation.getMembers().add(authenticatedUser);

        workstationRepository.save(workstation);

        return WorkstationMapper.INSTANCE.workstationToCreatedWorkstationDTO(workstation);
    }

    public void deleteWorkstation(Long workstationId) {
        workstationRepository.deleteById(workstationId);
    }

    public EditedWorkstationDTO editWorkstation(Long workstationId, EditWorkstationDTO editWorkstationDTO) {

        Workstation workstation = workstationRepository.findById(workstationId)
                .orElseThrow(() -> new NotFoundException("Workstation not found"));

        if (workstationRepository.existsByUsername(editWorkstationDTO.username()))
            throw new BadRequestException("Username already exists");

        if (editWorkstationDTO.name() != null) {
            workstation.setName(editWorkstationDTO.name());
        }

        if (editWorkstationDTO.username() != null) {
            workstation.setUsername(editWorkstationDTO.username());
        }

        workstationRepository.save(workstation);

        return WorkstationMapper.INSTANCE.workstationToEditedWorkstationDTO(workstation);
    }

    @Transactional
    public ProfileImgDTO editWorkstationProfileImg(Long workstationId, MultipartFile profileImg) {
        Workstation workstation = workstationRepository.findById(workstationId).orElseThrow(
                () -> new NotFoundException("Workstation not found")
        );

        if(profileImg.isEmpty())
            throw new BadRequestException("Profile image cannot be empty");

        if(profileImg.getContentType() != null && !profileImg.getContentType().startsWith("image"))
            throw new BadRequestException("Profile image must be an image");

        String folderName = "workstations/" + workstation.getId() + "/profile-img";
        String imgUrl = cloudinaryService.uploadImage(profileImg, folderName);
        workstation.setProfileImg(imgUrl);
        workstationRepository.save(workstation);
        return new ProfileImgDTO(workstation.getProfileImg());
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
        if (workstationId.equals(followId))
            throw new BadRequestException("You can't follow the same workstation");

        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Workstation not found");

        if(!workstationRepository.existsById(followId))
            throw new NotFoundException("Following workstation not found");

        if (workstationRepository.existsByIdAndFollowingWorkstations_Id(workstationId, followId))
            throw new BadRequestException("You're already following this workstation");

        workstationRepository.followWorkstation(workstationId, followId);
    }

    private void followUser(Long workstationId, Long userId) {
        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Workstation not found");

        if(!userRepository.existsById(userId))
            throw new NotFoundException("User not found");

        if (workstationRepository.existsByIdAndFollowingUsers_Id(workstationId, userId)) {
            throw new BadRequestException("You're already following this user");
        }

        workstationRepository.followUser(workstationId, userId);
    }

    private void unfollowUser(Long workstationId, Long userId) {
        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Workstation not found");

        if(!userRepository.existsById(userId))
            throw new NotFoundException("User not found");

        if (!workstationRepository.existsByIdAndFollowingUsers_Id(workstationId, userId)) {
            throw new BadRequestException("You're not following this user");
        }

        workstationRepository.unfollowUser(workstationId, userId);
    }

    private void unfollowWorkstation(Long workstationId, Long unfollowId) {
        if (workstationId.equals(unfollowId))
            throw new BadRequestException("You can't unfollow the same workstation");

        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Workstation not found");

        if(!workstationRepository.existsById(unfollowId))
            throw new NotFoundException("Following workstation not found");

        if (!workstationRepository.existsByIdAndFollowingWorkstations_Id(workstationId, unfollowId))
            throw new BadRequestException("You're not following this workstation");

        workstationRepository.unfollowWorkstation(workstationId, unfollowId);
    }

    public GetMembersDTO getMembers(Long workstationId, Pageable pageable) {
        if (!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Workstation not found");

        Page<WorkstationMemberDTO> usersPage = workstationRepository.findAllMembersByWorkstationId(workstationId, pageable);

        GetMembersDTO getMembersDTO = new GetMembersDTO();

        getMembersDTO.setWorkstationId(workstationId);
        getMembersDTO.setMembers(usersPage.getContent());
        getMembersDTO.setSize(pageable.getPageSize());
        getMembersDTO.setTotalElements(usersPage.getTotalElements());
        getMembersDTO.setTotalPages(usersPage.getTotalPages());
        getMembersDTO.setNumber(pageable.getPageNumber());

        return getMembersDTO;
    }

    public GetLeadersDTO getLeaders(Long workstationId, Pageable pageable) {
        if (!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Workstation not found");

        Page<User> usersPage = workstationRepository.findAllLeadersByWorkstationId(workstationId, pageable);

        List<WorkstationLeaderDTO> leaders = UserMapper.INSTANCE.userListToWorkstationLeaderDTOList(usersPage.getContent());

        GetLeadersDTO getLeadersDTO = new GetLeadersDTO();

        getLeadersDTO.setWorkstationId(workstationId);
        getLeadersDTO.setLeaders(leaders);
        getLeadersDTO.setSize(pageable.getPageSize());
        getLeadersDTO.setTotalElements(usersPage.getTotalElements());
        getLeadersDTO.setTotalPages(usersPage.getTotalPages());
        getLeadersDTO.setNumber(pageable.getPageNumber());

        return getLeadersDTO;
    }

    @Transactional
    public void addNewLeader(Long workstationId, Long leaderId) {
        if (!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Workstation not found");
        if (!userRepository.existsById(leaderId))
            throw new NotFoundException("User not found");

        workstationRepository.addNewLeader(workstationId, leaderId);
    }

    @Transactional
    public void removeLeader(Long workstationId, Long leaderId) {
        if (!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Workstation not found");
        if (!userRepository.existsById(leaderId))
            throw new NotFoundException("User not found");
        if (!workstationRepository.existsLeaderById(workstationId, leaderId))
            throw new BadRequestException("This user is not a leader of this workstation");

        workstationRepository.removeLeader(workstationId, leaderId);
    }

    public boolean isMember(Long workstationId) {
        User authenticatedUser = authUtil.getAuthenticatedUser();

        return workstationRepository.existsByIdAndMembers_Id(workstationId, authenticatedUser.getId());
    }

    public boolean isLeader(Long workstationId) {
        User authenticatedUser = authUtil.getAuthenticatedUser();

        return workstationRepository.existsByIdAndLeaders_Id(workstationId, authenticatedUser.getId());
    }

    public boolean isCreator(Long workstationId) {
        User authenticatedUser = authUtil.getAuthenticatedUser();
        Workstation workstation = workstationRepository.findById(workstationId).orElseThrow(() -> new NotFoundException("Workstation not found"));

        return workstation.getCreator().equals(authenticatedUser);
    }

}
