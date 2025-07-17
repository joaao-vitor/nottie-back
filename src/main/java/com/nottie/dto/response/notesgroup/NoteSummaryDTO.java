package com.nottie.dto.response.notesgroup;

import com.nottie.dto.response.note.NoteAuthorDTO;
import com.nottie.dto.response.note.NoteCategoryValueDTO;

import java.time.Instant;
import java.util.Set;

public class NoteSummaryDTO {
    private Long id;
    private String title;
    private NoteAuthorDTO creator;
    private Set<NoteAuthorDTO> collaborators;
    private Instant createdAt;
    private Set<NoteCategoryValueDTO> categoriesValues;

    public NoteSummaryDTO(Long id, String title, NoteAuthorDTO creator, Set<NoteAuthorDTO> collaborators, Instant createdAt, Set<NoteCategoryValueDTO> categoriesValues) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.collaborators = collaborators;
        this.createdAt = createdAt;
        this.categoriesValues = categoriesValues;
    }

    public NoteSummaryDTO() {
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

    public NoteAuthorDTO getCreator() {
        return creator;
    }

    public void setCreator(NoteAuthorDTO creator) {
        this.creator = creator;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<NoteAuthorDTO> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(Set<NoteAuthorDTO> collaborators) {
        this.collaborators = collaborators;
    }

    public Set<NoteCategoryValueDTO> getCategoriesValues() {
        return categoriesValues;
    }

    public void setCategoriesValues(Set<NoteCategoryValueDTO> categoriesValues) {
        this.categoriesValues = categoriesValues;
    }
}
