package com.nottie.dto.response.user;

public class AuthenticatedUserDTO {
    private Long id;
    private String name;
    private String username;
    private String profileImg;


    public AuthenticatedUserDTO(Long id, String name, String username, String profileImg) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.profileImg = profileImg;
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

}
