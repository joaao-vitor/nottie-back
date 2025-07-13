package com.nottie.dto.response.user;

import java.util.Objects;

public class SummaryDTO {
    private Long id;
    private String name;
    private String username;
    private String profileImg;
    private Long followersCount;
    private Long followingCount;
    private boolean isFollowing;
    private String summaryType;

    public SummaryDTO(Long id, String name, String username, String profileImg, Long followersCount, Long followingCount, boolean isFollowing, String summaryType) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.profileImg = profileImg;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.isFollowing = isFollowing;
        this.summaryType = summaryType;
    }

    public SummaryDTO() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public Long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Long followersCount) {
        this.followersCount = followersCount;
    }

    public Long getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Long followingCount) {
        this.followingCount = followingCount;
    }

    public boolean getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public String getSummaryType() {
        return summaryType;
    }

    public void setSummaryType(String summaryType) {
        this.summaryType = summaryType;
    }
}
