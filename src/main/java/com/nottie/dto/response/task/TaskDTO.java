package com.nottie.dto.response.task;

import com.nottie.dto.request.task.TaskCategoryValueDTO;
import com.nottie.dto.request.tasksgroup.TasksGroupCategoryDTO;
import com.nottie.model.StatusType;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private StatusType status;

    private TaskCreatorDTO creator;
    private Set<TaskCreatorDTO> members;
    private Instant createdAt;
    private Set<TasksGroupCategoryDTO> categories;
    private Set<TaskCategoryValueDTO> categoriesValues;

    public TaskDTO() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public TaskCreatorDTO getCreator() {
        return creator;
    }

    public void setCreator(TaskCreatorDTO creator) {
        this.creator = creator;
    }

    public Set<TaskCreatorDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<TaskCreatorDTO> members) {
        this.members = members;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<TaskCategoryValueDTO> getCategoriesValues() {
        return categoriesValues;
    }

    public void setCategoriesValues(Set<TaskCategoryValueDTO> categoriesValues) {
        this.categoriesValues = categoriesValues;
    }

    public Set<TasksGroupCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<TasksGroupCategoryDTO> categories) {
        this.categories = categories;
    }

}
