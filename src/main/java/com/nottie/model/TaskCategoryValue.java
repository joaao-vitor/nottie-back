package com.nottie.model;

import jakarta.persistence.*;

@Entity
@Table(name = "task_category_value")
public class TaskCategoryValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private TasksGroupCategory category;

    @Column(name = "text_value")
    private String textValue = "";

    @Column(name = "checkbox_value")
    private Boolean checkboxValue = false;

    @Column(name = "hex_color")
    private String hexColor = "#000";

    public TaskCategoryValue() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TasksGroupCategory getCategory() {
        return category;
    }

    public void setCategory(TasksGroupCategory category) {
        this.category = category;
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
}
