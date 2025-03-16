package com.nottie.repository;

import com.nottie.model.User;
import com.nottie.model.Workstation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WorkstationRepository extends JpaRepository<Workstation, Long> {

    Optional<Workstation> getWorkstationsByUsername(String username);

    Optional<Workstation> getWorkstationsById(Long id);

    boolean existsByIdAndLeaders_Id(Long workstationId, Long id);

    boolean existsByIdAndFollowingUsers_Id(Long workstationId, Long userId);

    boolean existsByIdAndFollowingWorkstations_Id(Long workstationId, Long followId);

    boolean existsByIdAndMembers_Id(Long workstationId, Long id);

    @Query("SELECT u FROM Workstation w JOIN w.members u WHERE w.id = :workstationId")
    Page<User> findAllMembersByWorkstationId(@Param("workstationId") Long workstationId, Pageable pageable);
}
