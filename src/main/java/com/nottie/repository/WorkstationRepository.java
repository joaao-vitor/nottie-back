package com.nottie.repository;

import com.nottie.dto.response.workstation.WorkstationMemberDTO;
import com.nottie.model.User;
import com.nottie.model.Workstation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkstationRepository extends JpaRepository<Workstation, Long> {

    Optional<Workstation> getWorkstationsByUsername(String username);

    Optional<Workstation> getWorkstationsById(Long id);

    Optional<List<Workstation>> getWorkstationsByMembers_Id(Long memberId);

    @Modifying
    @Query(value = "INSERT INTO workstation_follows_user (user_followed_id, workstation_id) VALUES (:userId, :workstationId)"
            , nativeQuery = true)
    void followUser(@Param("workstationId") Long workstationId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "INSERT INTO workstation_follows_workstation (workstation_followed_id, workstation_id) VALUES (:followId, :workstationId)",
            nativeQuery = true)
    void followWorkstation(@Param("workstationId") Long workstationId, @Param("followId") Long followId);

    @Modifying
    @Query(value = "DELETE FROM workstation_follows_user WHERE workstation_id = :workstationId AND user_followed_id = :userId",
            nativeQuery = true)
    void unfollowUser(@Param("workstationId") Long workstationId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM workstation_follows_workstation WHERE workstation_id = :workstationId AND workstation_followed_id = :unfollowId",
            nativeQuery = true
    )
    void unfollowWorkstation(@Param("workstationId") Long workstationId, @Param("unfollowId") Long unfollowId);

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

    @Query("SELECT u FROM Workstation w JOIN w.leaders u WHERE w.id = :workstationId")
    Page<User> findAllLeadersByWorkstationId(@Param("workstationId") Long workstationId, Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO workstation_leader (user_id, workstation_id) VALUES (:leaderId, :workstationId)",
            nativeQuery = true)
    void addNewLeader(@Param("workstationId") Long workstationId, @Param("leaderId") Long leaderId);

    @Modifying
    @Query(value = "INSERT INTO workstation_member (user_id, workstation_id) VALUES (:memberId, :workstationId)",
            nativeQuery = true)
    void addNewMember(@Param("workstationId") Long workstationId, @Param("memberId") Long memberId);

    @Query("SELECT count(l) > 0 FROM Workstation w JOIN w.leaders l WHERE w.id = :workstationId AND l.id = :leaderId")
    boolean existsLeaderById(@Param("workstationId") Long workstationId, @Param("leaderId") Long leaderId);

    @Modifying
    @Query(value = "DELETE FROM workstation_leader w WHERE w.user_id = :leaderId AND w.workstation_id = :workstationId",
            nativeQuery = true)
    void removeLeader(@Param("workstationId") Long workstationId, @Param("leaderId") Long leaderId);

    @Modifying
    @Query(value = "DELETE FROM workstation_member w WHERE w.user_id = :memberId AND w.workstation_id = :workstationId",
            nativeQuery = true)
    void removeMember(@Param("workstationId") Long workstationId, @Param("memberId") Long memberId);

    boolean existsByUsername(String username);

    @Query("SELECT count(f) FROM Workstation w JOIN w.followersUsers f WHERE w.id = :workstationId")
    Optional<Long> countFollowersUsersByWorkstationId(@Param("workstationId") Long workstationId);

    @Query("SELECT count(f) FROM Workstation w JOIN w.followingUsers f WHERE w.id = :workstationId")
    Optional<Long> countFollowingUsersByWorkstationId(@Param("workstationId") Long workstationId);

    @Query("SELECT count(f) FROM Workstation w JOIN w.followingWorkstations f WHERE w.id = :workstationId")
    Optional<Long> countFollowingWorkstationsByWorkstationId(@Param("workstationId") Long workstationId);

    @Query("SELECT count(f) FROM Workstation w JOIN w.followersWorkstations f WHERE w.id = :workstationId")
    Optional<Long> countFollowersWorkstationsByWorkstationId(@Param("workstationId") Long workstationId);

    Optional<Workstation> findByUsername(String username);

    boolean existsByIdAndFollowersUsers_Id(Long id, Long followersUsersId);

    boolean existsByIdAndFollowersWorkstations_Id(Long id, Long followersWorkstationsId);
}
