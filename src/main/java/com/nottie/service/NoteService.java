package com.nottie.service;

import com.nottie.dto.request.note.*;
import com.nottie.dto.response.note.NoteCategoryValueDTO;
import com.nottie.dto.response.note.NoteDTO;
import com.nottie.dto.response.note.SearchNoteDTO;
import com.nottie.dto.response.notesgroup.NotesGroupCategoryDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.exception.UnauthorizedException;
import com.nottie.mapper.NoteMapper;
import com.nottie.mapper.NotesGroupMapper;
import com.nottie.model.*;
import com.nottie.repository.NoteCategoryValueRepository;
import com.nottie.repository.NoteRepository;
import com.nottie.repository.NotesGroupCategoryRepository;
import com.nottie.repository.NotesGroupRepository;
import com.nottie.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NotesGroupCategoryRepository notesGroupCategoryRepository;
    private final NoteRepository noteRepository;
    private final NotesGroupRepository notesGroupRepository;
    private final NoteCategoryValueRepository noteCategoryValueRepository;
    private final WorkstationService workstationService;
    private final AuthUtil authUtil;

    public NoteService(NotesGroupCategoryRepository notesGroupCategoryRepository, NoteRepository noteRepository, NotesGroupRepository notesGroupRepository, NoteCategoryValueRepository noteCategoryValueRepository, WorkstationService workstationService, AuthUtil authUtil) {
        this.notesGroupCategoryRepository = notesGroupCategoryRepository;
        this.noteRepository = noteRepository;
        this.notesGroupRepository = notesGroupRepository;
        this.noteCategoryValueRepository = noteCategoryValueRepository;
        this.workstationService = workstationService;
        this.authUtil = authUtil;
    }

    @Transactional
    public List<SearchNoteDTO> searchNoteByWorkstation(String title, Long workstationId) {
        List<Note> notes = noteRepository.findTop5ByTitleContainingIgnoreCaseAndNotesGroup_Workstation_Id(title, workstationId);
        return NoteMapper.INSTANCE.noteToSearchNoteDTOS(notes);
    }

    @Transactional
    public List<SearchNoteDTO> searchNoteByUser(String title) {
        User user = authUtil.getAuthenticatedUser();
        List<Note> notes = noteRepository.findTop5ByCreator_IdAndTitleContainingIgnoreCase(user.getId(), title);
        return NoteMapper.INSTANCE.noteToSearchNoteDTOS(notes);
    }

    public NoteDTO createNote(NewNoteDTO newNoteDTO) {
        Note note = new Note();
        note.setTitle(newNoteDTO.getTitle());
        note.setNotesGroup(notesGroupRepository.findById(newNoteDTO.getNotesGroupId()).orElseThrow(() -> new BadRequestException("NotesGroup not found")));
        note.setContent(null);

        noteRepository.save(note);
        return NoteMapper.INSTANCE.noteToNoteDTO(note);
    }

    public void editSingleCategory(Long noteId, Long categoryValueId, EditSingleCategoryValueDTO editSingleCategoryValueDTO) {
        noteRepository.findById(noteId).orElseThrow(() -> new NotFoundException("NotesGroupCategory not found"));

        NoteCategoryValue noteCategoryValue = noteCategoryValueRepository.findById(categoryValueId).orElseThrow(() -> new NotFoundException("CategoryValue not found"));

        if (!noteCategoryValue.getCategory().getType().equals(editSingleCategoryValueDTO.type()))
            throw new BadRequestException("A categoria não é a mesma");
        if (!noteCategoryValue.getNote().getId().equals(noteId))
            throw new BadRequestException("Essa categoria não é dessa nota");

        CategoryType type = noteCategoryValue.getCategory().getType();
        if (type.equals(CategoryType.TEXT)) {
            noteCategoryValue.setTextValue(editSingleCategoryValueDTO.textValue());
        } else if (type.equals(CategoryType.CHECKBOX)) {
            noteCategoryValue.setCheckboxValue(editSingleCategoryValueDTO.checkboxValue());
        } else if (type.equals(CategoryType.TAG)) {
            noteCategoryValue.setHexColor(editSingleCategoryValueDTO.hexColor());
            noteCategoryValue.setTextValue(editSingleCategoryValueDTO.textValue());
        }

        noteCategoryValueRepository.save(noteCategoryValue);
    }

    public NoteDTO getSingleNote(Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new NotFoundException("NotesGroupCategory not found"));
        NoteDTO noteDTO = NoteMapper.INSTANCE.noteToNoteDTO(note);
        List<NotesGroupCategory> categoryList = notesGroupCategoryRepository.findAllByGroup_Id(note.getNotesGroup().getId());
        List<NotesGroupCategoryDTO> categoryDTOS = NotesGroupMapper.INSTANCE.notesGroupToNotesGroupCategoryDTO(categoryList);
        noteDTO.setCategories(categoryDTOS);
        return noteDTO;
    }

    public void verifyAccess(Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new NotFoundException("Note not found "));
        Workstation workstation = note.getNotesGroup().getWorkstation();

        User user = note.getNotesGroup().getUser();
        if (workstation != null) {
            if (!workstationService.isMember(workstation.getId()))
                throw new UnauthorizedException("Você não é membro dessa estação");
        } else {
            User userAuth = authUtil.getAuthenticatedUser();
            if (!user.getId().equals(userAuth.getId()))
                throw new UnauthorizedException("Você não é o dono desse grupo de anotações");
        }
    }

    public void editContent(Long noteId, byte[] content) {
        Note note = noteRepository.findById((noteId)).orElseThrow(() -> new NotFoundException("Note not found"));

        User user = authUtil.getAuthenticatedUser();

        note.getCollaborators().add(user);

        note.setContent(content);
        noteRepository.save(note);
    }

    public void editNoteTitle(Long noteId, EditNoteDTO editNoteDTO) {
        Note note = noteRepository.findById((noteId)).orElseThrow(() -> new NotFoundException("Note not found"));

        note.setTitle(editNoteDTO.title());
        noteRepository.save(note);
    }

    public NoteCategoryValueDTO addCategoryValue(Long noteId, Long categoryId, NewCategoryValueDTO noteCatValueDTO) {
        Note note = noteRepository.findById((noteId)).orElseThrow(() -> new NotFoundException("Note not found"));
        NotesGroupCategory notesGroupCategory = notesGroupCategoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));

        if (!note.getNotesGroup().getId().equals(notesGroupCategory.getGroup().getId())) {
            throw new BadRequestException("Essa anotação não pertence a essa categoria");
        }

        NoteCategoryValue noteCategoryValue = new NoteCategoryValue();
        noteCategoryValue.setCategory(notesGroupCategory);
        noteCategoryValue.setNote(note);
        noteCategoryValue.setHexColor(noteCatValueDTO.hexColor());
        noteCategoryValue.setTextValue(noteCatValueDTO.textValue());
        noteCategoryValue.setCheckboxValue(noteCatValueDTO.checkboxValue());
        noteCategoryValueRepository.save(noteCategoryValue);

        return NoteMapper.INSTANCE.noteCategoryValueToNoteCategoryValueDTO(noteCategoryValue);
    }

    public void deleteCategoryValue(Long noteId, Long categoryId, Long valueId) {
        Note note = noteRepository.findById((noteId)).orElseThrow(() -> new NotFoundException("Note not found"));
        NotesGroupCategory notesGroupCategory = notesGroupCategoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        NoteCategoryValue noteCategoryValue = noteCategoryValueRepository.findById((valueId)).orElseThrow(() -> new NotFoundException("CategoryValue not found"));

        if (!note.getNotesGroup().getId().equals(notesGroupCategory.getGroup().getId())) {
            throw new BadRequestException("Grupo de anotação não bate");
        }

        if (!noteCategoryValue.getNote().getId().equals(noteId)) {
            throw new BadRequestException("Essa categoria não é desta anotação");
        }
        noteCategoryValueRepository.deleteById(valueId);
    }


    public NoteCategoryValueDTO editCategoryValue(Long noteId, Long categoryId, NewCategoryValueDTO noteCatValueDTO, Long valueId) {
        Note note = noteRepository.findById((noteId)).orElseThrow(() -> new NotFoundException("Note not found"));
        NotesGroupCategory notesGroupCategory = notesGroupCategoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        NoteCategoryValue noteCategoryValue = noteCategoryValueRepository.findById((valueId)).orElseThrow(() -> new NotFoundException("CategoryValue not found"));

        if (!note.getNotesGroup().getId().equals(notesGroupCategory.getGroup().getId())) {
            throw new BadRequestException("Grupo de anotação não bate");
        }

        if (!noteCategoryValue.getNote().getId().equals(noteId)) {
            throw new BadRequestException("Essa categoria não é desta anotação");
        }
        if (noteCatValueDTO.hexColor() != null) noteCategoryValue.setHexColor(noteCatValueDTO.hexColor());
        if (noteCatValueDTO.textValue() != null) noteCategoryValue.setTextValue(noteCatValueDTO.textValue());
        if (noteCatValueDTO.checkboxValue() != null)
            noteCategoryValue.setCheckboxValue(noteCatValueDTO.checkboxValue());
        noteCategoryValueRepository.save(noteCategoryValue);

        return NoteMapper.INSTANCE.noteCategoryValueToNoteCategoryValueDTO(noteCategoryValue);
    }

}
