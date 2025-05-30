package com.nottie.repository;

import com.nottie.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    @Query("SELECT ud from UserDetail ud WHERE ud.user.email ILIKE :email")
    Optional<UserDetail> findByEmail(@Param("email") String email);
}
