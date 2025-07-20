package com.nottie.dto.response.post;

import com.nottie.dto.response.user.SummaryDTO;

public class PostCommentResponseDTO {
    private Long id;
    private String content;
    private SummaryDTO creator;
    private SummaryDTO workstation;

    public PostCommentResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SummaryDTO getCreator() {
        return creator;
    }

    public void setCreator(SummaryDTO creator) {
        this.creator = creator;
    }

    public SummaryDTO getWorkstation() {
        return workstation;
    }

    public void setWorkstation(SummaryDTO workstation) {
        this.workstation = workstation;
    }
}
