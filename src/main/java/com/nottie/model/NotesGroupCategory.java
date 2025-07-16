package com.nottie.model;

import jakarta.persistence.*;

@Entity
@Table(name = "notes_group_category")
public class NotesGroupCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @ManyToOne
    @JoinColumn(name = "notes_group_id")
    private NotesGroup group;

    public NotesGroupCategory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public NotesGroup getGroup() {
        return group;
    }

    public void setGroup(NotesGroup group) {
        this.group = group;
    }
}
