package com.nottie.dto.request.task;

import com.nottie.dto.request.tasksgroup.TasksGroupCategoryDTO;

public class TaskCategoryValueDTO {

    private Long id;
    private String textValue;
    private Boolean checkboxValue;
    private String hexColor;
    private TasksGroupCategoryDTO category;

    public TaskCategoryValueDTO(Long id, String textValue, Boolean checkboxValue, String hexColor, TasksGroupCategoryDTO category) {
        this.id = id;
        this.textValue = textValue;
        this.checkboxValue = checkboxValue;
        this.hexColor = hexColor;
        this.category = category;
    }

    public TaskCategoryValueDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public Boolean getCheckboxValue() {
        return checkboxValue;
    }

    public void setCheckboxValue(Boolean checkboxValue) {
        this.checkboxValue = checkboxValue;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public TasksGroupCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(TasksGroupCategoryDTO category) {
        this.category = category;
    }
}
