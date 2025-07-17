package com.nottie.dto.request.note;

public class NewNoteDTO {
    private String title;
    private Long notesGroupId;

    public NewNoteDTO(String title, Long notesGroupId) {
        this.title = title;
        this.notesGroupId = notesGroupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getNotesGroupId() {
        return notesGroupId;
    }

    public void setNotesGroupId(Long notesGroupId) {
        this.notesGroupId = notesGroupId;
    }
}
