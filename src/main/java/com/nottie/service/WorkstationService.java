package com.nottie.service;

import com.nottie.dto.response.user.SearchUserDTO;
import com.nottie.dto.response.workstation.WorkstationAuthDTO;
import com.nottie.dto.response.user.SummaryDTO;
import com.nottie.dto.request.workstation.CreateWorkstationDTO;
import com.nottie.dto.request.workstation.EditWorkstationDTO;
import com.nottie.dto.request.workstation.GetLeadersDTO;
import com.nottie.dto.request.workstation.WorkstationLeaderDTO;
import com.nottie.dto.response.workstation.*;
import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.exception.UnauthorizedException;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class WorkstationService {
    private final WorkstationRepository workstationRepository;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public Set<SearchUserDTO> searchMembers(Long workstationId, String name) {
        Workstation workstation = workstationRepository.findById(workstationId).orElseThrow(() -> new NotFoundException("Workstation not found"));

        Set<User> members = workstation.getMembers().stream().filter((member) -> {
            if(member.getName().toLowerCase().contains(name.toLowerCase())){
                return true;
            } else return member.getUsername().toLowerCase().contains(name.toLowerCase());
        }).collect(Collectors.toSet());

        return UserMapper.INSTANCE.usersToSearchUserDTOS(members);
    }

    public enum FollowType {USER, WORKSTATION}

    public WorkstationService(WorkstationRepository workstationRepository, AuthUtil authUtil, UserRepository userRepository, CloudinaryService cloudinaryService) {
        this.workstationRepository = workstationRepository;
        this.authUtil = authUtil;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public WorkstationAuthDTO getWorkstationAuth(Long workstationId) {
        Workstation workstation = workstationRepository.findById(workstationId).orElseThrow(() -> new NotFoundException("Estação de trabalho não encontrada"));

        boolean leader = isLeader(workstationId);
        boolean creator = isCreator(workstationId);
        WorkstationAuthDTO workstationAuthDTO = WorkstationMapper.INSTANCE.workstationToWorkstationAuthDTO(workstation);
        workstationAuthDTO.setIsLeader(leader);
        workstationAuthDTO.setIsCreator(creator);

        return workstationAuthDTO;
    }

    private SummaryDTO buildInitialSummary(Workstation workstation) {

        SummaryDTO summaryDTO = WorkstationMapper.INSTANCE.workstationToSummaryDTO(workstation);

        Long followersUserCount = workstationRepository.countFollowersUsersByWorkstationId(workstation.getId()).orElseThrow();
        Long followersWorkstationCount = workstationRepository.countFollowersWorkstationsByWorkstationId(workstation.getId()).orElseThrow();
        summaryDTO.setFollowersCount(followersUserCount + followersWorkstationCount);

        Long followingUserCount = workstationRepository.countFollowingUsersByWorkstationId(workstation.getId()).orElseThrow();
        Long followingWorkstationCount = workstationRepository.countFollowingWorkstationsByWorkstationId(workstation.getId()).orElseThrow();
        summaryDTO.setFollowingCount(followingUserCount + followingWorkstationCount);

        summaryDTO.setSummaryType("workstation");
        return summaryDTO;
    }

    public SummaryDTO getWorkstationSummaryAsUser(String username) {
        User authenticatedUser = authUtil.getAuthenticatedUser();
        Workstation workstation = workstationRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException("Estação de trabalho não encontrado.")
        );

        SummaryDTO summaryDTO = buildInitialSummary(workstation);

        boolean isFollowing = workstationRepository.existsByIdAndFollowersUsers_Id(workstation.getId(), authenticatedUser.getId());
        summaryDTO.setIsFollowing(isFollowing);

        return summaryDTO;
    }

    public SummaryDTO getWorkstationSummaryAsWorkstation(Long workstationId, String username) {
        Workstation workstation = workstationRepository.findByUsername(username).orElseThrow(
                () -> new BadRequestException("Estação de trabalho não encontrado.")
        );

        SummaryDTO summaryDTO = buildInitialSummary(workstation);

        boolean isFollowing = workstationRepository.existsByIdAndFollowersWorkstations_Id(workstation.getId(), workstationId);
        summaryDTO.setIsFollowing(isFollowing);

        return summaryDTO;
    }
    @Transactional
    public CreatedWorkstationDTO createWorkstation(CreateWorkstationDTO createWorkstationDTO) {
        if (createWorkstationDTO.name() == null || createWorkstationDTO.name().isEmpty())
            throw new BadRequestException("O campo nome não pode estar em branco.");

        if (createWorkstationDTO.username() == null || createWorkstationDTO.username().isEmpty())
            throw new BadRequestException("O campo nome de usuário não pode estar em branco.");

        if (userRepository.getUserByUsername(createWorkstationDTO.username()).isPresent()
                || workstationRepository.getWorkstationsByUsername(createWorkstationDTO.username()).isPresent())
            throw new BadRequestException("Este nome de usuário já existe.");

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
                .orElseThrow(() -> new NotFoundException("Estação de trabalho não encontrada."));

        Workstation workstationFoundByUsername = workstationRepository.findByUsername(editWorkstationDTO.username()).orElse(null);

        if (workstationFoundByUsername != null && !Objects.equals(workstationFoundByUsername.getId(), workstationId))
            throw new BadRequestException("Nome de usuário já existente.");

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
                () -> new NotFoundException("Estação de trabalho não encontrada.")
        );

        if(profileImg.isEmpty())
            throw new BadRequestException("A imagem de perfil é obrigatória.");

        if(profileImg.getContentType() != null && !profileImg.getContentType().startsWith("image"))
            throw new BadRequestException("A imagem de perfil deve ser uma imagem válida.");

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
            throw new BadRequestException("Você não pode seguir a mesma estação de trabalho.");

        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Estação de trabalho não encontrada.");

        if(!workstationRepository.existsById(followId))
            throw new NotFoundException("Estação de trabalho a ser seguida não encontrada.");

        if (workstationRepository.existsByIdAndFollowingWorkstations_Id(workstationId, followId))
            throw new BadRequestException("Você já está seguindo esta estação de trabalho.");

        workstationRepository.followWorkstation(workstationId, followId);
    }

    private void followUser(Long workstationId, Long userId) {
        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Estação de trabalho não encontrada.");

        if(!userRepository.existsById(userId))
            throw new NotFoundException("Usuário não encontrado.");

        if (workstationRepository.existsByIdAndFollowingUsers_Id(workstationId, userId)) {
            throw new BadRequestException("Você já está seguindo este usuário.");
        }

        workstationRepository.followUser(workstationId, userId);
    }

    private void unfollowUser(Long workstationId, Long userId) {
        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Estação de trabalho não encontrada.");

        if(!userRepository.existsById(userId))
            throw new NotFoundException("Usuário não encontrado.");

        if (!workstationRepository.existsByIdAndFollowingUsers_Id(workstationId, userId)) {
            throw new BadRequestException("Você não está seguindo este usuário.");
        }

        workstationRepository.unfollowUser(workstationId, userId);
    }

    private void unfollowWorkstation(Long workstationId, Long unfollowId) {
        if (workstationId.equals(unfollowId))
            throw new BadRequestException("Você não pode parar de seguir a mesma estação de trabalho.");

        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Estação de trabalho não encontrada.");

        if(!workstationRepository.existsById(unfollowId))
            throw new NotFoundException("Estação de trabalho a ser seguida não encontrada.");

        if (!workstationRepository.existsByIdAndFollowingWorkstations_Id(workstationId, unfollowId))
            throw new BadRequestException("Você não está seguindo esta estação de trabalho.");

        workstationRepository.unfollowWorkstation(workstationId, unfollowId);
    }

    public GetMembersDTO getMembers(Long workstationId, Pageable pageable) {
        Workstation workstation = workstationRepository.getWorkstationsById(workstationId).orElseThrow(() -> new NotFoundException("Estação de trabalho não encontrada"));

        User creator = workstation.getCreator();

        WorkstationMemberDTO creatorMember = UserMapper.INSTANCE.userToWorkstationMemberDTO(creator);

        Page<WorkstationMemberDTO> usersPage = workstationRepository.findAllMembersByWorkstationId(workstationId, pageable);

        GetMembersDTO getMembersDTO = new GetMembersDTO();

        getMembersDTO.setWorkstationId(workstationId);
        getMembersDTO.setMembers(usersPage.getContent());
        getMembersDTO.setCreator(creatorMember);
        getMembersDTO.setSize(pageable.getPageSize());
        getMembersDTO.setTotalElements(usersPage.getTotalElements());
        getMembersDTO.setTotalPages(usersPage.getTotalPages());
        getMembersDTO.setNumber(pageable.getPageNumber());

        return getMembersDTO;
    }

    public GetLeadersDTO getLeaders(Long workstationId, Pageable pageable) {
        if (!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Estação de trabalho não encontrada.");

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
            throw new NotFoundException("Estação de trabalho não encontrada.");
        if (!userRepository.existsById(leaderId))
            throw new NotFoundException("Usuário não encontrado.");
        if (workstationRepository.existsByIdAndLeaders_Id(workstationId, leaderId))
            throw new BadRequestException("O usuário já é líder");

        boolean isMember = workstationRepository.existsByIdAndMembers_Id(workstationId, leaderId);

        if(!isMember) {
            workstationRepository.addNewMember(workstationId, leaderId);
        }

        workstationRepository.addNewLeader(workstationId, leaderId);
    }

    @Transactional
    public void removeLeader(Long workstationId, Long leaderId) {
        if (!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Estação de trabalho não encontrada.");
        if (!userRepository.existsById(leaderId))
            throw new NotFoundException("Usuário não encontrado.");
        if (!workstationRepository.existsByIdAndLeaders_Id(workstationId, leaderId))
            throw new BadRequestException("O usuário não é líder");

        workstationRepository.removeLeader(workstationId, leaderId);
    }

    @Transactional
    public void addNewMember(Long workstationId, Long memberId) {

        if (!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Estação de trabalho não encontrada.");
        if (!userRepository.existsById(memberId))
            throw new NotFoundException("Usuário não encontrado.");
        if (workstationRepository.existsByIdAndMembers_Id(workstationId, memberId))
            throw new BadRequestException("O usuário já é membro");

        boolean isLeader = workstationRepository.existsByIdAndLeaders_Id(workstationId, memberId);

        if(isLeader) {
            throw new BadRequestException("Você não pode adicionar um líder.");
        }

        workstationRepository.addNewMember(workstationId, memberId);
    }

    @Transactional
    public void removeMember(Long workstationId, Long memberId) {
        if (!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Estação de trabalho não encontrada aqui.");
        if (!userRepository.existsById(memberId))
            throw new NotFoundException("Usuário não encontrado.");
        if (!workstationRepository.existsByIdAndMembers_Id(workstationId, memberId))
            throw new BadRequestException("O usuário não é membro");

        boolean isLeader = workstationRepository.existsByIdAndLeaders_Id(workstationId, memberId);

        if(isLeader) {
            if(isCreator(workstationId)){
                workstationRepository.removeLeader(workstationId, memberId);
            } else {
                throw new UnauthorizedException("Você não pode excluir um líder");
            }
        }

        workstationRepository.removeMember(workstationId, memberId);
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
        Workstation workstation = workstationRepository.findById(workstationId).orElseThrow(() -> new NotFoundException("Estação de trabalho não encontrada."));

        return workstation.getCreator().equals(authenticatedUser);
    }

}
