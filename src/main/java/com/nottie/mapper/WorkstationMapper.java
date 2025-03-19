package com.nottie.mapper;

import com.nottie.dto.request.workstation.CreateWorkstationDTO;
import com.nottie.dto.response.user.SummaryDTO;
import com.nottie.dto.response.workstation.CreatedWorkstationDTO;
import com.nottie.dto.response.workstation.EditedWorkstationDTO;
import com.nottie.model.Workstation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkstationMapper {
    WorkstationMapper INSTANCE = Mappers.getMapper(WorkstationMapper.class);

    // Create Workstation
    Workstation createWorkstationDTOToWorkstation(CreateWorkstationDTO createWorkstationDTO);
    CreatedWorkstationDTO workstationToCreatedWorkstationDTO(Workstation workstation);

    EditedWorkstationDTO workstationToEditedWorkstationDTO(Workstation workstation);

    @Mapping(target = "followingCount", ignore = true)
    @Mapping(target = "followersCount", ignore = true)
    SummaryDTO workstationToSummaryDTO(Workstation workstation);
}
