package com.nottie.dto.response.notesgroup;


import com.nottie.dto.response.note.NoteDTO;

import java.util.List;

public class NotesGroupDTO {
    private Long id;
    private String title;
    private List<NotesGroupCategoryDTO> categories;

    private List<NoteDTO> notes;

    public NotesGroupDTO(Long id, String title, List<NotesGroupCategoryDTO> categories, List<NoteDTO> notes) {
        this.id = id;
        this.title = title;
        this.categories = categories;
        this.notes = notes;
    }

    public NotesGroupDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NotesGroupCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<NotesGroupCategoryDTO> categories) {
        this.categories = categories;
    }

    public List<NoteDTO> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDTO> notes) {
        this.notes = notes;
    }
}
