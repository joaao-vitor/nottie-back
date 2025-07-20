package com.nottie.dto.response.post;

import com.nottie.dto.response.user.SummaryDTO;

public class NewPostResponseDTO {
    private Long id;
    private String content;
    private Long likesCount;
    private Long repostsCount;
    private Long commentsCount;
    private SummaryDTO creator;
    private SummaryDTO workstation;

    public NewPostResponseDTO() {
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

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public Long getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(Long repostsCount) {
        this.repostsCount = repostsCount;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
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
