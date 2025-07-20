package com.nottie.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks_group_category")
public class TasksGroupCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @ManyToOne
    @JoinColumn(name = "tasks_group_id")
    private TasksGroup group;

    public TasksGroupCategory() {
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

    public TasksGroup getGroup() {
        return group;
    }

    public void setGroup(TasksGroup group) {
        this.group = group;
    }
}
