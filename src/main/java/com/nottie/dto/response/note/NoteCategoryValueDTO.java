package com.nottie.dto.response.note;

import com.nottie.dto.response.notesgroup.NotesGroupCategoryDTO;

public class NoteCategoryValueDTO {
    private Long id;
    private String textValue;
    private Boolean checkboxValue;
    private String hexColor;
    private NotesGroupCategoryDTO category;


    public NoteCategoryValueDTO(Long id, String textValue, Boolean checkboxValue, String hexColor, NotesGroupCategoryDTO category) {
        this.id = id;
        this.textValue = textValue;
        this.checkboxValue = checkboxValue;
        this.hexColor = hexColor;
        this.category = category;
    }

    public NoteCategoryValueDTO() {
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

    public NotesGroupCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(NotesGroupCategoryDTO category) {
        this.category = category;
    }
}
