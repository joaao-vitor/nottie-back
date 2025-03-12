package com.nottie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "workstation")
public class Workstation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String username;
    @Column(name = "profile_img", nullable = false)
    private String profileImg = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png";

    @ManyToMany
    @JoinTable(
            name = "workstation_follows_user",
            joinColumns = {@JoinColumn(name = "workstation_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_followed_id")}
    )
    @JsonIgnore
    private Set<User> followingUsers;
    @ManyToMany
    @JoinTable(
            name = "workstation_follows_workstation",
            joinColumns = {@JoinColumn(name = "workstation_id")},
            inverseJoinColumns = {@JoinColumn(name = "workstation_followed_id")}
    )
    @JsonIgnore
    private Set<Workstation> followingWorkstations;

    @ManyToMany
    @JoinTable(
            name = "workstation_member",
            joinColumns = {@JoinColumn(name = "workstation_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> members;
    @ManyToMany
    @JoinTable(
            name = "workstation_leader",
            joinColumns = {@JoinColumn(name = "workstation_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> leaders;

    @CreatedBy
    @ManyToOne
    @JoinColumn(
            name = "creator_id",
            nullable = false
    )
    private User creator;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Workstation() {
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

    public Set<User> getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(Set<User> followingUsers) {
        this.followingUsers = followingUsers;
    }

    public Set<Workstation> getFollowingWorkstations() {
        return followingWorkstations;
    }

    public void setFollowingWorkstations(Set<Workstation> followingWorkstations) {
        this.followingWorkstations = followingWorkstations;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<User> getLeaders() {
        return leaders;
    }

    public void setLeaders(Set<User> leaders) {
        this.leaders = leaders;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
