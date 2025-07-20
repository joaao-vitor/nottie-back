package com.nottie.dto.response.timeline;

import com.nottie.dto.response.user.SummaryDTO;
import com.nottie.model.InteractionType;

import java.time.Instant;

public class TimelineItemDTO {
    private Long id;
    private String content;
    private InteractionType type;
    private SummaryDTO creator;
    private SummaryDTO workstation;
    private Long likesCount;
    private Long repostsCount;
    private Long commentsCount;
    private Instant createdAt;
    private NoteTimelinePostDTO note;

    private boolean liked;
    private boolean reposted;

    public TimelineItemDTO() {
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isReposted() {
        return reposted;
    }

    public void setReposted(boolean reposted) {
        this.reposted = reposted;
    }

    public NoteTimelinePostDTO getNote() {
        return note;
    }

    public void setNote(NoteTimelinePostDTO note) {
        this.note = note;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
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

    public InteractionType getType() {
        return type;
    }

    public void setType(InteractionType type) {
        this.type = type;
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
}
