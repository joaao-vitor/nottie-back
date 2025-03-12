package com.nottie.service;

import com.nottie.dto.request.workstation.CreateWorkstationDTO;
import com.nottie.dto.response.workstation.CreatedWorkstationDTO;
import com.nottie.exception.BadRequestException;
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

    public WorkstationService(WorkstationRepository workstationRepository, AuthUtil authUtil, UserRepository userRepository) {
        this.workstationRepository = workstationRepository;
        this.authUtil = authUtil;
        this.userRepository = userRepository;
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

}
