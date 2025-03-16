package com.nottie.dto.response.workstation;

import java.util.Objects;

public class WorkstationMemberDTO{
        private Long id;
        private String name;
        private String username;
        private String profileImg;
        private boolean isLeader;

    public WorkstationMemberDTO() {
    }

    public WorkstationMemberDTO(Long id, String name, String username, String profileImg, boolean isLeader) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.profileImg = profileImg;
        this.isLeader = isLeader;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WorkstationMemberDTO that = (WorkstationMemberDTO) o;
        return isLeader == that.isLeader && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(username, that.username) && Objects.equals(profileImg, that.profileImg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, profileImg, isLeader);
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

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }
}
