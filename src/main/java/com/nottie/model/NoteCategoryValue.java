package com.nottie.model;

import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "note_category_value")
public class NoteCategoryValue{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private NotesGroupCategory category;

    @Column(name = "text_value")
    private String textValue = "";

    @Column(name = "checkbox_value")
    private Boolean checkboxValue = false;

    @Column(name = "hex_color")
    private String hexColor = "#000";

    public NoteCategoryValue() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public NotesGroupCategory getCategory() {
        return category;
    }

    public void setCategory(NotesGroupCategory category) {
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
