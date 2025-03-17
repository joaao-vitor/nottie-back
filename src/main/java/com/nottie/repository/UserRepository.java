package com.nottie.repository;

import com.nottie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> getUserById(Long userId);

    @Modifying
    @Query(value = "INSERT INTO user_follow_user (user_followed_id, user_id) VALUES (:followId, :userId)"
            , nativeQuery = true)
    void followUser(@Param("userId") Long userId, @Param("followId") Long followId);

    @Modifying
    @Query(value = "INSERT INTO user_follow_workstation (user_id, workstation_id) VALUES (:userId, :workstationId)",
            nativeQuery = true)
    void followWorkstation(@Param("userId") Long userId, @Param("workstationId") Long workstationId);

    @Modifying
    @Query(value = "DELETE FROM user_follow_user WHERE user_id = :userId AND user_followed_id = :unfollowId",
            nativeQuery = true)
    void unfollowUser(@Param("userId") Long userId, @Param("unfollowId") Long unfollowId);

    @Modifying
    @Query(value = "DELETE FROM user_follow_workstation WHERE workstation_id = :workstationId AND user_id = :userId",
            nativeQuery = true
    )
    void unfollowWorkstation(@Param("userId") Long userId, @Param("workstationId") Long workstationId);

    @Query("SELECT count(f) FROM User u JOIN u.followersUsers f WHERE u.id = :userId")
    Optional<Long> countFollowersUsersByUserId(@Param("userId") Long userId);

    @Query("SELECT count(f) FROM User u JOIN u.followingUsers f WHERE u.id = :userId")
    Optional<Long> countFollowingUsersByUserId(@Param("userId") Long userId);

    @Query("SELECT count(f) FROM User u JOIN u.followingWorkstations f WHERE u.id = :userId")
    Optional<Long> countFollowingWorkstationsByUserId(@Param("userId") Long userId);

    @Query("SELECT count(f) FROM User u JOIN u.followersWorkstations f WHERE u.id = :userId")
    Optional<Long> countFollowersWorkstationsByUserId(@Param("userId") Long userId);

    Optional<User> getUserByUsername(String username);

    boolean existsByIdAndFollowingUsers_Id(Long id, Long followersUsers_id);

    boolean existsByIdAndFollowingWorkstations_Id(Long id, Long workstationId);

    boolean existsByUsername(String username);
}
