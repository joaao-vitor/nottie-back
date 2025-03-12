package com.nottie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "profile_img")
    private String profileImg = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png";

    @ManyToMany
    @JoinTable(
            name = "user_follow_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_followed_id")}
    )
    @JsonIgnore
    private List<User> followingUsers;

    @ManyToMany(mappedBy = "followingUsers")
    private List<User> followersUsers;

    @ManyToMany
    @JoinTable(
            name = "user_follow_workstation",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "workstation_id")}
    )
    private List<Workstation> followingWorkstations;

    @ManyToMany(mappedBy = "followingUsers")
    private List<Workstation> followersWorkstations;



    public User() {}

    public List<Workstation> getFollowingWorkstations() {
        return followingWorkstations;
    }

    public void setFollowingWorkstations(List<Workstation> followingWorkstations) {
        this.followingWorkstations = followingWorkstations;
    }

    public List<Workstation> getFollowersWorkstations() {
        return followersWorkstations;
    }

    public void setFollowersWorkstations(List<Workstation> followersWorkstations) {
        this.followersWorkstations = followersWorkstations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public List<User> getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(List<User> followingUsers) {
        this.followingUsers = followingUsers;
    }

    public List<User> getFollowersUsers() {
        return followersUsers;
    }

    public void setFollowersUsers(List<User> followersUsers) {
        this.followersUsers = followersUsers;
    }
}
