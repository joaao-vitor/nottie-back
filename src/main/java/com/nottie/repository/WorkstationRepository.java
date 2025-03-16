package com.nottie.repository;

import com.nottie.dto.response.workstation.WorkstationMemberDTO;
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

    @Query("SELECT " +
            "new com.nottie.dto.response.workstation.WorkstationMemberDTO(" +
            "u.id, u.name, u.username, u.profileImg, " +
            "CASE WHEN " +
            "(SELECT count(l) FROM Workstation w2 JOIN w2.leaders l WHERE u.id = l.id AND w.id = :workstationId) " +
            "> 0 THEN true ELSE false END" +
            ") FROM Workstation w JOIN w.members u WHERE w.id = :workstationId")
    Page<WorkstationMemberDTO> findAllMembersByWorkstationId(@Param("workstationId") Long workstationId, Pageable pageable);
}
