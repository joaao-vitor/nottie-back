package com.nottie.service;

import com.nottie.dto.request.note.NewNoteDTO;
import com.nottie.dto.request.note.EditSingleCategoryValueDTO;
import com.nottie.dto.response.note.NoteDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.model.CategoryType;
import com.nottie.model.Note;
import com.nottie.model.NoteCategoryValue;
import com.nottie.model.NotesGroupCategory;
import com.nottie.repository.NoteCategoryValueRepository;
import com.nottie.repository.NoteRepository;
import com.nottie.repository.NotesGroupCategoryRepository;
import com.nottie.repository.NotesGroupRepository;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final NotesGroupCategoryRepository notesGroupCategoryRepository;
    private final NoteRepository noteRepository;
    private final NotesGroupRepository notesGroupRepository;
    private final NoteCategoryValueRepository noteCategoryValueRepository;

    public NoteService(NotesGroupCategoryRepository notesGroupCategoryRepository, NoteRepository noteRepository, NotesGroupRepository notesGroupRepository, NoteCategoryValueRepository noteCategoryValueRepository) {
        this.notesGroupCategoryRepository = notesGroupCategoryRepository;
        this.noteRepository = noteRepository;
        this.notesGroupRepository = notesGroupRepository;
        this.noteCategoryValueRepository = noteCategoryValueRepository;
    }

    public void createNote(NewNoteDTO newNoteDTO) {
        Note note = new Note();
        note.setTitle(newNoteDTO.getTitle());
        note.setNotesGroup(notesGroupRepository.findById(newNoteDTO.getNotesGroupId()).orElseThrow(() -> new BadRequestException("NotesGroup not found")));
        note.setContent("");

        noteRepository.save(note);

        System.out.println(newNoteDTO.getCategoriesValues().toString());
        newNoteDTO.getCategoriesValues().forEach(categoryValue -> {
            NoteCategoryValue noteCategoryValue = new NoteCategoryValue();
            NotesGroupCategory category = notesGroupCategoryRepository.findById(categoryValue.category().id()).orElseThrow(() -> new BadRequestException("Category not found"));
            noteCategoryValue.setCategory(category);
            noteCategoryValue.setTextValue(categoryValue.textValue());
            noteCategoryValue.setCheckboxValue(categoryValue.checkboxValue());
            noteCategoryValue.setHexColor(categoryValue.hexColor());
            noteCategoryValue.setNote(note);

            noteCategoryValueRepository.save(noteCategoryValue);
        });
    }

    public void editSingleCategory(Long noteId, Long categoryValueId, EditSingleCategoryValueDTO editSingleCategoryValueDTO) {
        noteRepository.findById(noteId).orElseThrow(() -> new NotFoundException("NotesGroupCategory not found"));

        NoteCategoryValue noteCategoryValue = noteCategoryValueRepository.findById(categoryValueId).orElseThrow(() -> new NotFoundException("CategoryValue not found"));

        if(!noteCategoryValue.getCategory().getType().equals(editSingleCategoryValueDTO.type())) throw new BadRequestException("A categoria não é a mesma");
        if(!noteCategoryValue.getNote().getId().equals(noteId)) throw new BadRequestException("Essa categoria não é dessa nota");

        CategoryType type = noteCategoryValue.getCategory().getType();
        if(type.equals(CategoryType.TEXT)) {
            noteCategoryValue.setTextValue(editSingleCategoryValueDTO.textValue());
        } else if(type.equals(CategoryType.CHECKBOX)) {
            noteCategoryValue.setCheckboxValue(editSingleCategoryValueDTO.checkboxValue());
        } else if(type.equals(CategoryType.TAG)) {
            noteCategoryValue.setHexColor(editSingleCategoryValueDTO.hexColor());
            noteCategoryValue.setTextValue(editSingleCategoryValueDTO.textValue());
        }

        noteCategoryValueRepository.save(noteCategoryValue);
    }

    public NoteDTO getSingleNote(Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new NotFoundException("NotesGroupCategory not found"));

    }
}
