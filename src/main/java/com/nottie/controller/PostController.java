package com.nottie.controller;

import com.nottie.dto.request.post.DeletePostInteractionDTO;
import com.nottie.dto.request.post.NewPostCommentDTO;
import com.nottie.dto.request.post.NewPostDTO;
import com.nottie.dto.request.post.NewPostInteractionDTO;
import com.nottie.dto.response.post.PostCommentResponseDTO;
import com.nottie.dto.response.timeline.TimelineItemDTO;
import com.nottie.service.PostService;
import com.nottie.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId, @RequestParam(required = false) Long workstationId) {
        TimelineItemDTO post = postService.getPost(postId, workstationId);
        return ResponseUtil.buildSuccessResponse(post, "Post fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/timeline")
    public ResponseEntity<?> getUserTimeline(Pageable pageable) {
        Page<TimelineItemDTO> timeline = postService.getUserTimeline(pageable);
        return ResponseUtil.buildSuccessResponse(timeline, "Timeline fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/timeline/workstation/{workstationId}")
    public ResponseEntity<?> getWorkstationTimeline(Pageable pageable, @PathVariable Long workstationId) {
        Page<TimelineItemDTO> timeline = postService.getWorkstationTimeline(pageable, workstationId);
        return ResponseUtil.buildSuccessResponse(timeline, "Timeline fetched successfully", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createNewPost(@RequestBody @Valid NewPostDTO newPostDTO) {
        TimelineItemDTO response = postService.createNewPost(newPostDTO);
        return ResponseUtil.buildSuccessResponse(response, "Post created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/interaction")
    public ResponseEntity<?> interactWithPost(
            @PathVariable Long postId,
            @RequestBody @Valid NewPostInteractionDTO newPostInteractionDTO
    ) {
        postService.newPostInteraction(postId, newPostInteractionDTO);
        return ResponseUtil.buildSuccessResponse("Interaction created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getPostComments(@PathVariable Long postId, @RequestParam(required = false) Long workstationId) {
        List<PostCommentResponseDTO> comments = postService.getCommentsByPostId(postId, workstationId);
        return ResponseUtil.buildSuccessResponse(comments, "Comments fetched successfully", HttpStatus.OK);
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<?> commentOnPost(
            @PathVariable Long postId,
            @RequestBody @Valid NewPostCommentDTO newPostCommentDTO
    ) {
        PostCommentResponseDTO postCommentResponseDTO = postService.newPostComment(postId, newPostCommentDTO);
        return ResponseUtil.buildSuccessResponse(postCommentResponseDTO, "Comment created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{postId}/interaction/delete")
    public ResponseEntity<?> deletePostInteraction(@PathVariable Long postId, @RequestBody DeletePostInteractionDTO deletePostInteractionDTO) {
        postService.deletePostInteraction(postId, deletePostInteractionDTO);
        return ResponseUtil.buildSuccessResponse("Interaction deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseUtil.buildSuccessResponse("Post deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deletePostComment(@PathVariable Long commentId) {
        postService.deletePostComment(commentId);
        return ResponseUtil.buildSuccessResponse("Comment deleted successfully", HttpStatus.OK);
    }
}
