package com.nottie.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "notes_group")
public class NotesGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @OneToMany(mappedBy = "group")
    private List<NotesGroupCategory> categories;

    // not null = workstation notes group
    @ManyToOne
    @JoinColumn(name = "workstation_id")
    private Workstation workstation;

    // not null = user's personal notes group
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public NotesGroup() {
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

    public List<NotesGroupCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<NotesGroupCategory> categories) {
        this.categories = categories;
    }

    public Workstation getWorkstation() {
        return workstation;
    }

    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
