package com.nottie.service;

import com.nottie.dto.request.notesgroup.CreateNotesGroupCategoryDTO;
import com.nottie.dto.request.notesgroup.CreateNotesGroupDTO;
import com.nottie.dto.request.notesgroup.EditSingleCategoryDTO;
import com.nottie.dto.response.note.NoteCategoryValueDTO;
import com.nottie.dto.response.notesgroup.GetAllNotesGroupDTO;
import com.nottie.dto.response.notesgroup.NoteSummaryDTO;
import com.nottie.dto.response.notesgroup.NotesGroupDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.exception.UnauthorizedException;
import com.nottie.mapper.NoteMapper;
import com.nottie.mapper.NotesGroupMapper;
import com.nottie.mapper.UserMapper;
import com.nottie.model.*;
import com.nottie.repository.*;
import com.nottie.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotesGroupService {

    private final NotesGroupRepository notesGroupRepository;
    private final WorkstationRepository workstationRepository;
    private final NoteRepository noteRepository;
    private final NotesGroupCategoryRepository notesGroupCategoryRepository;
    private final AuthUtil authUtil;
    private final WorkstationService workstationService;
    private final NoteCategoryValueRepository noteCategoryValueRepository;

    public NotesGroupService(NotesGroupRepository notesGroupRepository, WorkstationRepository workstationRepository, NoteRepository noteRepository, NotesGroupCategoryRepository notesGroupCategoryRepository, AuthUtil authUtil, WorkstationService workstationService, NoteCategoryValueRepository noteCategoryValueRepository) {
        this.notesGroupRepository = notesGroupRepository;
        this.workstationRepository = workstationRepository;
        this.noteRepository = noteRepository;
        this.notesGroupCategoryRepository = notesGroupCategoryRepository;
        this.authUtil = authUtil;
        this.workstationService = workstationService;
        this.noteCategoryValueRepository = noteCategoryValueRepository;
    }

    public void createNotesGroup(CreateNotesGroupDTO notesGroupDTO) {
        NotesGroup notesGroup = new NotesGroup();
        notesGroup.setTitle(notesGroupDTO.title());
        System.out.println(notesGroupDTO);
        Workstation workstation;

        if (notesGroupDTO.workstationId() != null) {
            workstation = workstationRepository.findById(notesGroupDTO.workstationId()).orElseThrow(() -> new NotFoundException("Workstation not found"));

            if (!workstationService.isLeader(workstation.getId()))
                throw new UnauthorizedException("Você não é lider dessa estação");

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

    @Transactional
    public NotesGroupDTO getNotesGroupById(Long notesGroupId) {
        NotesGroup notesGroup = notesGroupRepository.findById(notesGroupId).orElseThrow(() -> new NotFoundException("NotesGroup not found"));
        NotesGroupDTO notesGroupDTO = NotesGroupMapper.INSTANCE.notesGroupToNotesGroupDTO(notesGroup);

        List<Note> notes = noteRepository.findSummaryByNotesGroup_Id(notesGroupId);
        List<NoteSummaryDTO> noteDTOS = new ArrayList<>();

        for (Note note : notes) {
            NoteSummaryDTO noteSummaryDTO = new NoteSummaryDTO();
            noteSummaryDTO.setId(note.getId());
            noteSummaryDTO.setTitle(note.getTitle());
            noteSummaryDTO.setCollaborators(UserMapper.INSTANCE.usersToNoteAuthorDTOS(note.getCollaborators()));
            noteSummaryDTO.setCreator(UserMapper.INSTANCE.userToNoteAuthorDTO(note.getCreator()));
            noteSummaryDTO.setCategoriesValues(NoteMapper.INSTANCE.noteCategoryValueListToNoteCategoryValueDTOS(note.getCategoriesValues()));
            noteSummaryDTO.setCreatedAt(note.getCreatedAt());

            noteDTOS.add(noteSummaryDTO);
        }

        notesGroupDTO.setNotes(noteDTOS);

        return notesGroupDTO;
    }

    public void newNotesGroupCategory(Long notesGroupId, CreateNotesGroupCategoryDTO notesGroupCategoryDTO) {
        NotesGroup notesGroup = notesGroupRepository.findById(notesGroupId).orElseThrow(() -> new NotFoundException("NotesGroup not found"));

        if (notesGroup.getWorkstation() != null) {
            if (!workstationService.isLeader(notesGroup.getWorkstation().getId()))
                throw new UnauthorizedException("Você não é lider dessa estação");
        } else {
            User user = authUtil.getAuthenticatedUser();
            if (!notesGroup.getUser().getId().equals(user.getId()))
                throw new UnauthorizedException("Você não é o dono desse grupo de anotações");
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
        if (!notesGroupCategory.getGroup().getId().equals(notesGroupId))
            throw new BadRequestException("Essa categoria não pertence a esse grupo");

        if (editSingleCategoryDTO.type() != null) {
            notesGroupCategory.setType(editSingleCategoryDTO.type());
        }
        if (editSingleCategoryDTO.name() != null) {
            notesGroupCategory.setName(editSingleCategoryDTO.name());
        }
        notesGroupCategoryRepository.save(notesGroupCategory);
    }
    @Transactional
    public List<NoteCategoryValueDTO> getNotesGroupCategoryTags(Long notesGroupId, Long categoryId) {
        NotesGroupCategory notesGroupCategory = notesGroupCategoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("NotesGroupCategory not found"));

        if (!notesGroupCategory.getGroup().getId().equals(notesGroupId)) {
            throw new BadRequestException("Categoria não equivale ao grupo de anotações");
        }

        List<NoteCategoryValue> noteCategoryValues = noteCategoryValueRepository.findAllByCategory_Id(categoryId);

        Set<String> seenKeys = new HashSet<>();

        List<NoteCategoryValue> distinctValues = noteCategoryValues.stream()
                .filter(value -> {
                    String key = value.getTextValue() + "::" + value.getHexColor();
                    return seenKeys.add(key);
                })
                .collect(Collectors.toList());

        return NoteMapper.INSTANCE.noteCategoryValueListToNoteCategoryValueDTOS(distinctValues);
    }

    public boolean verifyMember(Long notesGroupId) {
        NotesGroup notesGroup = notesGroupRepository.findById(notesGroupId).orElseThrow(() -> new NotFoundException("NotesGroup not found"));
        User user = authUtil.getAuthenticatedUser();
        if (notesGroup.getWorkstation() != null)
            return workstationService.isMember(notesGroup.getWorkstation().getId());
        else
            return notesGroup.getUser().getId().equals(user.getId());
    }

    public boolean verifyLeader(Long notesGroupId) {
        NotesGroup notesGroup = notesGroupRepository.findById(notesGroupId).orElseThrow(() -> new NotFoundException("NotesGroup not found"));
        User user = authUtil.getAuthenticatedUser();
        if (notesGroup.getWorkstation() != null)
            return workstationService.isLeader(notesGroup.getWorkstation().getId());
        else
            return notesGroup.getUser().getId().equals(user.getId());
    }
}
