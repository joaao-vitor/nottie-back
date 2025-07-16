package com.nottie.dto.request.note;

import java.util.List;

public class NewNoteDTO {
    private String title;
    private List<NoteCategoryValue> categoriesValues;
    private Long notesGroupId;

    public NewNoteDTO(String title, List<NoteCategoryValue> categoriesValues, Long notesGroupId) {
        this.title = title;
        this.categoriesValues = categoriesValues;
        this.notesGroupId = notesGroupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NoteCategoryValue> getCategoriesValues() {
        return categoriesValues;
    }

    public void setCategoriesValues(List<NoteCategoryValue> categoriesValues) {
        this.categoriesValues = categoriesValues;
    }

    public Long getNotesGroupId() {
        return notesGroupId;
    }

    public void setNotesGroupId(Long notesGroupId) {
        this.notesGroupId = notesGroupId;
    }
}
