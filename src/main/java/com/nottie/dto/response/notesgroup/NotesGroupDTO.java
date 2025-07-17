package com.nottie.dto.response.notesgroup;


import java.util.List;

public class NotesGroupDTO {
    private Long id;
    private String title;
    private List<NotesGroupCategoryDTO> categories;

    private List<NoteSummaryDTO> notes;

    public NotesGroupDTO(Long id, String title, List<NotesGroupCategoryDTO> categories, List<NoteSummaryDTO> notes) {
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

    public List<NoteSummaryDTO> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteSummaryDTO> notes) {
        this.notes = notes;
    }
}
