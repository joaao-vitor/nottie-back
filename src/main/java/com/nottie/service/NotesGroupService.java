package com.nottie.service;

import com.nottie.dto.request.notesgroup.CreateNotesGroupCategoryDTO;
import com.nottie.dto.request.notesgroup.CreateNotesGroupDTO;
import com.nottie.dto.request.notesgroup.EditSingleCategoryDTO;
import com.nottie.dto.response.note.NoteDTO;
import com.nottie.dto.response.notesgroup.GetAllNotesGroupDTO;
import com.nottie.dto.response.notesgroup.NotesGroupDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.exception.UnauthorizedException;
import com.nottie.mapper.NoteMapper;
import com.nottie.mapper.NotesGroupMapper;
import com.nottie.model.*;
import com.nottie.repository.NoteRepository;
import com.nottie.repository.NotesGroupCategoryRepository;
import com.nottie.repository.NotesGroupRepository;
import com.nottie.repository.WorkstationRepository;
import com.nottie.util.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesGroupService {

    private final NotesGroupRepository notesGroupRepository;
    private final WorkstationRepository workstationRepository;
    private final NoteRepository noteRepository;
    private final NotesGroupCategoryRepository notesGroupCategoryRepository;
    private final AuthUtil authUtil;
    private final WorkstationService workstationService;

    public NotesGroupService(NotesGroupRepository notesGroupRepository, WorkstationRepository workstationRepository, NoteRepository noteRepository, NotesGroupCategoryRepository notesGroupCategoryRepository, AuthUtil authUtil, WorkstationService workstationService) {
        this.notesGroupRepository = notesGroupRepository;
        this.workstationRepository = workstationRepository;
        this.noteRepository = noteRepository;
        this.notesGroupCategoryRepository = notesGroupCategoryRepository;
        this.authUtil = authUtil;
        this.workstationService = workstationService;
    }

    public void createNotesGroup(CreateNotesGroupDTO notesGroupDTO) {
        NotesGroup notesGroup = new NotesGroup();
        notesGroup.setTitle(notesGroupDTO.title());

        Workstation workstation;

        if(notesGroupDTO.workstationId() != null) {
            workstation = workstationRepository.findById(notesGroupDTO.workstationId()).orElseThrow(() -> new NotFoundException("Workstation not found"));

            if(!workstationService.isLeader(workstation.getId())) throw new UnauthorizedException("Você não é lider dessa estação");

            notesGroup.setWorkstation(workstation);
        } else {
            User user = authUtil.getAuthenticatedUser();
            notesGroup.setUser(user);
        }

        notesGroupRepository.save(notesGroup);
    }

    public List<GetAllNotesGroupDTO> getAllNotesGroupByWorkstation(Long workstationId) {
        workstationRepository.findById(workstationId).orElseThrow(() -> new NotFoundException("Workstation not found"));

        List<NotesGroup> notesGroups = notesGroupRepository.findAllByWorkstation_Id(workstationId);
        return NotesGroupMapper.INSTANCE.notesGroupToAllNotesGroupDTO(notesGroups);
    }

    public List<GetAllNotesGroupDTO> getAllNotesGroupByUser() {
        User user = authUtil.getAuthenticatedUser();

        List<NotesGroup> notesGroups = notesGroupRepository.findAllByUser_Id(user.getId());
        return NotesGroupMapper.INSTANCE.notesGroupToAllNotesGroupDTO(notesGroups);
    }

    public NotesGroupDTO getNotesGroupById(Long notesGroupId) {
        NotesGroup notesGroup = notesGroupRepository.findById(notesGroupId).orElseThrow(() -> new NotFoundException("NotesGroup not found"));
        NotesGroupDTO notesGroupDTO = NotesGroupMapper.INSTANCE.notesGroupToNotesGroupDTO(notesGroup);

        List<Note> notes = noteRepository.findAllByNotesGroup_Id(notesGroupId);
        List<NoteDTO> noteDTOS = NoteMapper.INSTANCE.noteListToDTO(notes);

        notesGroupDTO.setNotes(noteDTOS);

        return notesGroupDTO;
    }

    public void newNotesGroupCategory(Long notesGroupId, CreateNotesGroupCategoryDTO notesGroupCategoryDTO) {
        NotesGroup notesGroup = notesGroupRepository.findById(notesGroupId).orElseThrow(() -> new NotFoundException("NotesGroup not found"));

        if(notesGroup.getWorkstation() != null) {
            if(!workstationService.isLeader(notesGroup.getWorkstation().getId())) throw new UnauthorizedException("Você não é lider dessa estação");
        } else {
            User user = authUtil.getAuthenticatedUser();
            if(!notesGroup.getUser().getId().equals(user.getId())) throw new UnauthorizedException("Você não é o dono desse grupo de anotações");
        }

        NotesGroupCategory notesGroupCategory = new NotesGroupCategory();
        notesGroupCategory.setName(notesGroupCategoryDTO.name());
        notesGroupCategory.setType(notesGroupCategoryDTO.type());
        notesGroupCategory.setGroup(notesGroup);
        notesGroupCategoryRepository.save(notesGroupCategory);

        notesGroup.getCategories().add(notesGroupCategory);
        notesGroupRepository.save(notesGroup);
    }

    public void editSingleCategory(Long notesGroupId, Long categoryId, EditSingleCategoryDTO editSingleCategoryDTO) {
        NotesGroupCategory notesGroupCategory = notesGroupCategoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("NotesGroupCategory not found"));
        if(!notesGroupCategory.getGroup().getId().equals(notesGroupId)) throw new BadRequestException("Essa categoria não pertence a esse grupo");

        if(editSingleCategoryDTO.type() != null) {
            notesGroupCategory.setType(editSingleCategoryDTO.type());
        }
        if(editSingleCategoryDTO.name() != null) {
            notesGroupCategory.setName(editSingleCategoryDTO.name());
        }
        notesGroupCategoryRepository.save(notesGroupCategory);
    }
}
