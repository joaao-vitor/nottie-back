package com.nottie.dto.request.user;

import java.util.Objects;

public class UserSummaryDTO {
    private String id;
    private String name;
    private String username;
    private String profileImg;
    private Long followersCount;
    private Long followingCount;

    public UserSummaryDTO() {
    }

    public UserSummaryDTO(String id, String name, String username, String profileImg, Long followersCount, Long followingCount) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.profileImg = profileImg;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSummaryDTO that = (UserSummaryDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(username, that.username) && Objects.equals(profileImg, that.profileImg) && Objects.equals(followersCount, that.followersCount) && Objects.equals(followingCount, that.followingCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, profileImg, followersCount, followingCount);
    }
}
