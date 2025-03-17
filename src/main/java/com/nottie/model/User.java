package com.nottie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.Set;

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
    private Set<User> followingUsers;

    @ManyToMany(mappedBy = "followingUsers")
    private Set<User> followersUsers;

    @ManyToMany
    @JoinTable(
            name = "user_follow_workstation",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "workstation_id")}
    )
    private Set<Workstation> followingWorkstations;

    @ManyToMany(mappedBy = "followingUsers")
    private Set<Workstation> followersWorkstations;



    public User() {}

    public Set<Workstation> getFollowingWorkstations() {
        return followingWorkstations;
    }

    public void setFollowingWorkstations(Set<Workstation> followingWorkstations) {
        this.followingWorkstations = followingWorkstations;
    }

    public Set<Workstation> getFollowersWorkstations() {
        return followersWorkstations;
    }

    public void setFollowersWorkstations(Set<Workstation> followersWorkstations) {
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

    public Set<User> getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(Set<User> followingUsers) {
        this.followingUsers = followingUsers;
    }

    public Set<User> getFollowersUsers() {
        return followersUsers;
    }

    public void setFollowersUsers(Set<User> followersUsers) {
        this.followersUsers = followersUsers;
    }
}
