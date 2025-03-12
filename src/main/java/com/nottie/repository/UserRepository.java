package com.nottie.repository;

import com.nottie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> getUserById(Long userId);

    @Query("SELECT count(f) FROM User u JOIN u.followersUsers f WHERE u.id = :userId")
    Optional<Long> countFollowersUsersByUserId(@Param("userId") Long userId);

    @Query("SELECT count(f) FROM User u JOIN u.followingUsers f WHERE u.id = :userId")
    Optional<Long> countFollowingUsersByUserId(@Param("userId") Long userId);

    @Query("SELECT count(f) FROM User u JOIN u.followingWorkstations f WHERE u.id = :userId")
    Optional<Long> countFollowingWorkstationsByUserId(@Param("userId") Long userId);

    @Query("SELECT count(f) FROM User u JOIN u.followersWorkstations f WHERE u.id = :userId")
    Optional<Long> countFollowersWorkstationsByUserId(@Param("userId") Long userId);

    Optional<User> getUserByUsername(String username);
}
