package com.nottie.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "note")
@EntityListeners(AuditingEntityListener.class)
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedBy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    private User creator;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToMany
    @JoinTable(
            name = "note_collaborator",
            joinColumns = {@JoinColumn(name = "note_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> collaborators = Collections.singletonList(creator);

    @LastModifiedDate
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedAt;

    @OneToMany(mappedBy = "note")
    private List<NoteCategoryValue> categoriesValues;

    @ManyToOne
    @JoinColumn(name = "notesGroup_id")
    private NotesGroup notesGroup;
    public Note() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<User> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<User> collaborators) {
        this.collaborators = collaborators;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<NoteCategoryValue> getCategoriesValues() {
        return categoriesValues;
    }

    public void setCategoriesValues(List<NoteCategoryValue> categoriesValues) {
        this.categoriesValues = categoriesValues;
    }

    public NotesGroup getNotesGroup() {
        return notesGroup;
    }

    public void setNotesGroup(NotesGroup notesGroup) {
        this.notesGroup = notesGroup;
    }
}
