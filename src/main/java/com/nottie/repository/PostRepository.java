package com.nottie.repository;

import com.nottie.dto.util.timeline.TimelineItemProjection;
import com.nottie.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
            SELECT 
                pi.type, COUNT(*) 
            FROM post_interaction pi 
            WHERE pi.post_id = :postId AND pi.type IN ('LIKE', 'RETWEET')
            GROUP BY pi.type
            """, nativeQuery = true)
    List<Object[]> countLikesAndRetweetsByPostId(@Param("postId") Long postId);


    @Query("SELECT count(c) FROM Post p JOIN p.postComments c WHERE p.id = :postId")
    Optional<Long> countCommentsByPostId(@Param("postId") Long postId);

    @Query(value = """
            SELECT * FROM (
                SELECT DISTINCT ON (id) *
                FROM (
                      -- SEU BLOCO UNION ALL PERMANECE IGUAL AQUI
                      SELECT 
                        p.id AS id, 'POST' AS type, p.content, p.creator_id AS creatorId, 
                        p.workstation_id AS workstationId, p.note_id AS noteId, p.created_at AS createdAt
                      FROM post p WHERE p.creator_id IN (:userIds) and p.workstation_id is null
            
                      UNION ALL
            
                      SELECT 
                        pi.post_id AS id, pi.type, p.content, p.creator_id AS creatorId, 
                        p.workstation_id AS workstationId, p.note_id AS noteId, pi.created_at AS createdAt
                      FROM post_interaction pi JOIN post p ON p.id = pi.post_id
                      WHERE pi.creator_id IN (:userIds) AND pi.type IN ('LIKE', 'RETWEET') and p.workstation_id is null
            
                      UNION ALL
            
                      SELECT 
                        p.id AS id, 'POST' AS type, p.content, p.creator_id AS creatorId,
                        p.workstation_id AS workstationId, p.note_id AS noteId, p.created_at AS createdAt
                      FROM post p WHERE p.workstation_id IN (:workstationsIds)
            
                      UNION ALL
            
                      SELECT 
                        pi.post_id AS id, pi.type, p.content, p.creator_id AS creatorId,
                        p.workstation_id AS workstationId, p.note_id AS noteId, pi.created_at AS createdAt
                      FROM post_interaction pi JOIN post p ON p.id = pi.post_id
                      WHERE pi.workstation_id IN (:workstationsIds) AND pi.type IN ('LIKE', 'RETWEET')
                ) AS timeline
                ORDER BY id, createdAt DESC
            ) AS final_timeline
            -- ALTERAÇÃO PRINCIPAL AQUI 👇
            ORDER BY createdAt DESC, id DESC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<TimelineItemProjection> getWorkstationTimelineNative(
            @Param("userIds") Set<Long> userIds,
            @Param("workstationsIds") Set<Long> workstationsIds,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query(value = """
                SELECT COUNT(DISTINCT id) FROM (
                  SELECT p.id FROM post p WHERE p.creator_id IN (:userIds)
                  UNION
                  SELECT pi.post_id as id FROM post_interaction pi
                  WHERE pi.creator_id IN (:userIds) AND pi.type IN ('LIKE', 'RETWEET')
                  UNION
                  SELECT p.id FROM post p WHERE p.workstation_id IN (:workstationsIds)
                  UNION
                  SELECT pi.post_id as id FROM post_interaction pi
                  WHERE pi.workstation_id IN (:workstationsIds) AND pi.type IN ('LIKE', 'RETWEET')
                ) AS timeline
            """, nativeQuery = true)
    long countWorkstationTimelineTotal(@Param("userIds") Set<Long> userIds, @Param("workstationsIds") Set<Long> workstationsIds);

}
