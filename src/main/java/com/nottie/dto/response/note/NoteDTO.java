package com.nottie.dto.response.note;

import com.nottie.dto.response.notesgroup.NotesGroupCategoryDTO;

import java.time.Instant;
import java.util.List;

public class NoteDTO {
    private Long id;
    private String title;
    private byte[] content;
    private NoteAuthorDTO creator;
    private List<NoteAuthorDTO> collaborators;
    private Instant createdAt;
    private List<NoteCategoryValueDTO> categoriesValues;
    private List<NotesGroupCategoryDTO> categories;

    public NoteDTO() {
    }

    public NoteDTO(Long id, String title, byte[] content, NoteAuthorDTO creator, List<NoteAuthorDTO> collaborators, Instant createdAt, List<NoteCategoryValueDTO> categoriesValues, List<NotesGroupCategoryDTO> categories) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creator = creator;
        this.collaborators = collaborators;
        this.createdAt = createdAt;
        this.categoriesValues = categoriesValues;
        this.categories = categories;
    }

    public List<NotesGroupCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<NotesGroupCategoryDTO> categories) {
        this.categories = categories;
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public NoteAuthorDTO getCreator() {
        return creator;
    }

    public void setCreator(NoteAuthorDTO creator) {
        this.creator = creator;
    }

    public List<NoteAuthorDTO> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<NoteAuthorDTO> collaborators) {
        this.collaborators = collaborators;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }


    public List<NoteCategoryValueDTO> getCategoriesValues() {
        return categoriesValues;
    }

    public void setCategoriesValues(List<NoteCategoryValueDTO> categoriesValues) {
        this.categoriesValues = categoriesValues;
    }
}
