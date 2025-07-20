package com.nottie.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Table(name = "tasks_group")
@EntityListeners(AuditingEntityListener.class)
public class TasksGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "group")
    private List<TasksGroupCategory> categories;

    // not null = workstation tasks group
    @ManyToOne
    @JoinColumn(name = "workstation_id")
    private Workstation workstation;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    public TasksGroup() {
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

    public List<TasksGroupCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<TasksGroupCategory> categories) {
        this.categories = categories;
    }

    public Workstation getWorkstation() {
        return workstation;
    }

    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
