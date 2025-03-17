package com.nottie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "workstation")
@EntityListeners(AuditingEntityListener.class)
public class Workstation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(name = "profile_img", nullable = false)
    private String profileImg = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png";

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "workstation_follows_user",
            joinColumns = {@JoinColumn(name = "workstation_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_followed_id")}
    )
    @JsonIgnore
    private Set<User> followingUsers;

    @ManyToMany(mappedBy = "followingWorkstations", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<User> followersUsers;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "workstation_follows_workstation",
            joinColumns = {@JoinColumn(name = "workstation_id")},
            inverseJoinColumns = {@JoinColumn(name = "workstation_followed_id")}
    )
    @JsonIgnore
    private Set<Workstation> followingWorkstations;

    @ManyToMany(mappedBy = "followingWorkstations", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Workstation> followersWorkstations;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "workstation_member",
            joinColumns = {@JoinColumn(name = "workstation_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> members;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "workstation_leader",
            joinColumns = {@JoinColumn(name = "workstation_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> leaders;

    @CreatedBy
    @ManyToOne
    @JoinColumn(
            name = "creator_id"
    )
    private User creator;

    @CreatedDate
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(
            name = "updated_by"
    )
    private User updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedAt;

    public Workstation() {
        leaders = new HashSet<>();
        members = new HashSet<>();
    }

    public Set<User> getFollowersUsers() {
        return followersUsers;
    }

    public void setFollowersUsers(Set<User> followersUsers) {
        this.followersUsers = followersUsers;
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
