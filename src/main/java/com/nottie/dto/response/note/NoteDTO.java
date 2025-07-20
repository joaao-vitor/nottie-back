package com.nottie.dto.response.note;

import com.nottie.dto.response.notesgroup.NotesGroupCategoryDTO;

import java.time.Instant;
import java.util.List;

public class NoteDTO {
    private Long id;
    private String title;
    private byte[] content;
    private NoteAuthorDTO creator;
    private NoteAuthorDTO workstation;
    private List<NoteAuthorDTO> collaborators;
    private Instant createdAt;
    private List<NoteCategoryValueDTO> categoriesValues;
    private List<NotesGroupCategoryDTO> categories;
    private boolean published;

    public NoteDTO() {
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public NoteAuthorDTO getWorkstation() {
        return workstation;
    }

    public void setWorkstation(NoteAuthorDTO workstation) {
        this.workstation = workstation;
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
