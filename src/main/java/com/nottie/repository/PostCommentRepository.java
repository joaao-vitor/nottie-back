package com.nottie.repository;

import com.nottie.model.PostComment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostCommentRepository extends CrudRepository<PostComment, Long> {
    List<PostComment> findAllByPost_IdOrderByCreatedAtDesc(Long postId);
}
