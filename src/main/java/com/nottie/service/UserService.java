package com.nottie.service;

import com.nottie.dto.request.user.EditUserDTO;
import com.nottie.dto.response.user.SummaryDTO;
import com.nottie.dto.response.user.EditedUserDTO;
import com.nottie.dto.response.workstation.ProfileImgDTO;
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
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final WorkstationRepository workstationRepository;
    private final CloudinaryService cloudinaryService;

    public enum FollowType { USER, WORKSTATION }

    public UserService(UserRepository userRepository, AuthUtil authUtil, WorkstationRepository workstationRepository, CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.authUtil = authUtil;
        this.workstationRepository = workstationRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public EditedUserDTO updateUser(Long id, EditUserDTO editUserDTO) {
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if (!userAuthenticated.getId().equals(id))
            throw new UnauthorizedException("Você não é autorizado a atualizar esse usário");

        if (userRepository.existsByUsername(editUserDTO.username()) && !userAuthenticated.getUsername().equals(editUserDTO.username()))
            throw new BadRequestException("Nome de usuário já existe");

        userAuthenticated.setUsername(editUserDTO.username());
        userAuthenticated.setName(editUserDTO.name());
        userRepository.save(userAuthenticated);

        return UserMapper.INSTANCE.userToEditedUserDTO(userAuthenticated);
    }
    @Transactional
    public ProfileImgDTO editUserProfileImg(Long userId, MultipartFile profileImg) {
        User userAuthenticated = authUtil.getAuthenticatedUser();
        if (!userAuthenticated.getId().equals(userId))
            throw new UnauthorizedException("Você não é autorizado a editar esse usuário");

        if(profileImg.isEmpty())
            throw new BadRequestException("A imagem de perfil é obrigatória");

        if(profileImg.getContentType() != null && !profileImg.getContentType().startsWith("image"))
            throw new BadRequestException("A imagem de perfil deve ser uma imagem válida");

        String folderName = "users/" + userAuthenticated.getId() + "/profile-img";
        String imgUrl = cloudinaryService.uploadImage(profileImg, folderName);
        userAuthenticated.setProfileImg(imgUrl);
        userRepository.save(userAuthenticated);
        return new ProfileImgDTO(userAuthenticated.getProfileImg());
    }

    public void deleteUser(Long id) {
        User userAuthenticated = authUtil.getAuthenticatedUser();
        if(!userAuthenticated.getId().equals(id))
            throw new UnauthorizedException("Você não é autorizado a excluir esse usuário.");
        userRepository.delete(userAuthenticated);
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
            throw new BadRequestException("Você não pode seguir você mesmo.");

        if(!userRepository.existsById(followId))
            throw new NotFoundException("Usuário não encontrado.");

        System.out.println(userRepository.existsByIdAndFollowingUsers_Id(userAuthenticated.getId(), followId));
        if(userRepository.existsByIdAndFollowingUsers_Id(userAuthenticated.getId(), followId))
            throw new BadRequestException("Você já está seguindo esse usuário");

        userRepository.followUser(userAuthenticated.getId(), followId);
    }


    private void unfollowUser(Long unfollowId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if(userAuthenticated.getId().equals(unfollowId))
            throw new BadRequestException("Você não pode parar de seguir você mesmo.");


        if(!userRepository.existsById(unfollowId))
            throw new NotFoundException("Usuário não encontrado");

        if(!userRepository.existsByIdAndFollowingUsers_Id(userAuthenticated.getId(), unfollowId))
            throw new BadRequestException("Você não está seguindo esse usuário.");

        userRepository.unfollowUser(userAuthenticated.getId(), unfollowId);
    }

    private void followWorkstation(Long workstationId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Estação de trabalho não encontrada.");

        if(userRepository.existsByIdAndFollowingWorkstations_Id(userAuthenticated.getId(), workstationId))
            throw new NotFoundException("Você já está seguindo esta estação de trabalho.");

        userRepository.followWorkstation(userAuthenticated.getId(), workstationId);
    }

    private void unfollowWorkstation(Long workstationId){
        User userAuthenticated = authUtil.getAuthenticatedUser();

        if(!workstationRepository.existsById(workstationId))
            throw new NotFoundException("Estação de trabalho não encontrada.");

        if(!userRepository.existsByIdAndFollowingWorkstations_Id(userAuthenticated.getId(), workstationId))
            throw new NotFoundException("Você não está seguindo esta estação de trabalho.");

        userRepository.unfollowWorkstation(userAuthenticated.getId(), workstationId);
    }

    public SummaryDTO getUserSummary(Long id) {
        User user = userRepository.getUserById(id).orElseThrow(
                () -> new BadRequestException("Usuário não encontrado.")
        );

        SummaryDTO summaryDTO = UserMapper.INSTANCE.userToSummaryDTO(user);

        Long followersUserCount = userRepository.countFollowersUsersByUserId(user.getId()).orElseThrow();
        Long followersWorkstationCount = userRepository.countFollowersWorkstationsByUserId(user.getId()).orElseThrow();
        summaryDTO.setFollowersCount(followersUserCount + followersWorkstationCount);

        Long followingUserCount = userRepository.countFollowingUsersByUserId(user.getId()).orElseThrow();
        Long followingWorkstationCount = userRepository.countFollowingWorkstationsByUserId(user.getId()).orElseThrow();

        summaryDTO.setFollowingCount(followingUserCount + followingWorkstationCount);
        return summaryDTO;
    }
}
