package com.nottie.dto.response.workstation;

public class WorkstationAuthDTO{
    private Long id;
    private String name;
    private String username;
    private String profileImg;
    private boolean isLeader;
    private boolean isCreator;

    public WorkstationAuthDTO() {
    }

    public WorkstationAuthDTO(Long id, String name, String username, String profileImg, boolean isLeader, boolean isCreator) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.profileImg = profileImg;
        this.isLeader = isLeader;
        this.isCreator = isCreator;
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

    public boolean getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(boolean leader) {
        isLeader = leader;
    }

    public boolean getIsCreator() {
        return isCreator;
    }

    public void setIsCreator(boolean creator) {
        isCreator = creator;
    }
}