package com.nottie.repository;

import com.nottie.model.InteractionType;
import com.nottie.model.PostInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PostInteractionRepository extends JpaRepository<PostInteraction, Long> {
    @Query("SELECT CASE WHEN COUNT(pi) > 0 THEN true ELSE false END FROM PostInteraction pi WHERE pi.post.id = :postId AND pi.workstation.id = :workstationId AND pi.type = :type")
    boolean existsByPostIdAndWorkstationIdAndType(@Param("postId") Long postId,
                                                  @Param("workstationId") Long workstationId,
                                                  @Param("type") InteractionType type);


    @Query("SELECT CASE WHEN COUNT(pi) > 0 THEN true ELSE false END FROM PostInteraction pi WHERE pi.post.id = :postId AND pi.creator.id = :creatorId AND pi.type = :type")
    boolean existsByPostIdAndCreatorIdAndType(@Param("postId") Long postId,
                                                  @Param("creatorId") Long creatorId,
                                                  @Param("type") InteractionType type);

    PostInteraction findByPost_IdAndWorkstation_IdAndType(Long postId, Long workstationId, InteractionType type);

    boolean existsByPostIdAndCreatorIdAndTypeAndWorkstation_IdIsNull(Long postId, Long creatorId, InteractionType type);

    PostInteraction findByPost_IdAndCreator_IdAndTypeAndWorkstation_IdIsNull(Long postId, Long creatorId, InteractionType type);
}
