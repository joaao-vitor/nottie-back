package com.nottie.dto.response.tasksgroup;

import com.nottie.dto.request.tasksgroup.TasksGroupCategoryDTO;
import com.nottie.dto.response.task.TaskDTO;

import java.util.List;

public class TasksGroupDTO {

    private Long id;
    private String name;
    private List<TasksGroupCategoryDTO> categories;

    private List<TaskDTO> tasks;

    public TasksGroupDTO(Long id, String name, List<TasksGroupCategoryDTO> categories, List<TaskDTO> tasks) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.tasks = tasks;
    }

    public TasksGroupDTO() {
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

    public List<TasksGroupCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<TasksGroupCategoryDTO> categories) {
        this.categories = categories;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }
}
