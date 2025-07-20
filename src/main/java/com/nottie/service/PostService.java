package com.nottie.service;

import com.nottie.dto.request.post.DeletePostInteractionDTO;
import com.nottie.dto.request.post.NewPostCommentDTO;
import com.nottie.dto.request.post.NewPostDTO;
import com.nottie.dto.request.post.NewPostInteractionDTO;
import com.nottie.dto.response.post.PostCommentResponseDTO;
import com.nottie.dto.response.timeline.TimelineItemDTO;
import com.nottie.dto.util.timeline.TimelineItemProjection;
import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.exception.UnauthorizedException;
import com.nottie.mapper.NoteMapper;
import com.nottie.mapper.PostMapper;
import com.nottie.mapper.UserMapper;
import com.nottie.mapper.WorkstationMapper;
import com.nottie.model.*;
import com.nottie.repository.*;
import com.nottie.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final WorkstationRepository workstationRepository;
    private final NoteRepository noteRepository;
    private final PostRepository postRepository;
    private final AuthUtil authUtil;
    private final WorkstationService workstationService;
    private final PostInteractionRepository postInteractionRepository;
    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public PostService(WorkstationRepository workstationRepository, NoteRepository noteRepository, PostRepository postRepository, AuthUtil authUtil, WorkstationService workstationService, PostInteractionRepository postInteractionRepository, PostCommentRepository postCommentRepository, UserRepository userRepository, UserService userService) {
        this.workstationRepository = workstationRepository;
        this.noteRepository = noteRepository;
        this.postRepository = postRepository;
        this.authUtil = authUtil;
        this.workstationService = workstationService;
        this.postInteractionRepository = postInteractionRepository;
        this.postCommentRepository = postCommentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public TimelineItemDTO createNewPost(NewPostDTO newPostDTO) {
        Post post = new Post();
        if (newPostDTO.workstationId() != null) {
            // Post veio de uma workstation
            Workstation workstation = workstationRepository.findById(newPostDTO.workstationId()).orElseThrow(() -> new NotFoundException("Estação de trabalho não encontrada."));
            if (!workstationService.isLeader(workstation.getId()))
                throw new UnauthorizedException("Você não está autorizado a fazer uma publicação como estação de trabalho.");
            post.setWorkstation(workstation);
        }

        if (newPostDTO.noteId() != null) {
            // Tem uma note vinculada
            User authUser = authUtil.getAuthenticatedUser();
            Note note = noteRepository.findById(newPostDTO.noteId()).orElseThrow(() -> new NotFoundException("Anotação não encontrada"));
            if (newPostDTO.workstationId() != null && !note.getNotesGroup().getWorkstation().getId().equals(newPostDTO.workstationId())) {
                throw new UnauthorizedException("Você não está autorizado a postar essa anotação");
            } else if (!note.getCreator().getId().equals(authUser.getId())) {
                throw new UnauthorizedException("Você não está autorizado a postar essa anotação");
            }
            note.setPublished(true);
            post.setNote(note);
        }

        post.setContent(newPostDTO.content());

        postRepository.save(post);

        TimelineItemDTO newPost = PostMapper.INSTANCE.postToTimelineItemDTO(post);

        newPost.setLikesCount(0L);
        newPost.setCommentsCount(0L);
        newPost.setRepostsCount(0L);

        if (newPostDTO.workstationId() != null) {
            newPost.setCreator(userService.getUserSummaryAsWorkstation(post.getWorkstation().getId(), post.getCreator().getUsername()));
            newPost.setWorkstation(workstationService.getWorkstationSummaryAsWorkstation(post.getWorkstation().getId(), post.getWorkstation().getUsername()));
        } else {
            newPost.setCreator(userService.getUserSummaryAsUser(post.getCreator().getUsername()));
        }

        return newPost;
    }

    public void newPostInteraction(Long postId, NewPostInteractionDTO newPostInteractionDTO) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post não encontrado"));

        PostInteraction postInteraction = new PostInteraction();
        postInteraction.setPost(post);

        if (newPostInteractionDTO.workstationId() != null) {
            if (postInteractionRepository.existsByPostIdAndWorkstationIdAndType(postId, newPostInteractionDTO.workstationId(), newPostInteractionDTO.type())) {
                throw new BadRequestException("Você já interagiu com esse post");
            }

            Workstation workstation = workstationRepository.findById(newPostInteractionDTO.workstationId()).orElseThrow(() -> new NotFoundException("Estação de trabalho não encontrada."));

            if (!workstationService.isLeader(workstation.getId()))
                throw new UnauthorizedException("Você não está autorizado a fazer uma publicação como estação de trabalho.");

            postInteraction.setWorkstation(workstation);
        } else {
            User authUser = authUtil.getAuthenticatedUser();
            if (postInteractionRepository.existsByPostIdAndCreatorIdAndTypeAndWorkstation_IdIsNull(postId, authUser.getId(), newPostInteractionDTO.type())) {
                throw new BadRequestException("Você já interagiu com esse post");
            }
        }

        postInteraction.setType(newPostInteractionDTO.type());

        postInteractionRepository.save(postInteraction);
    }

    public PostCommentResponseDTO newPostComment(Long postId, NewPostCommentDTO newPostCommentDTO) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post não encontrado"));

        PostComment comment = new PostComment();
        comment.setPost(post);

        if (newPostCommentDTO.workstationId() != null) {
            Workstation workstation = workstationRepository.findById(newPostCommentDTO.workstationId()).orElseThrow(() -> new NotFoundException("Estação de trabalho não encontrada."));

            if (!workstationService.isLeader(workstation.getId()))
                throw new UnauthorizedException("Você não está autorizado a fazer uma publicação como estação de trabalho.");

            comment.setWorkstation(workstation);
        }

        comment.setContent(newPostCommentDTO.content());
        postCommentRepository.save(comment);

        PostCommentResponseDTO newComment = PostMapper.INSTANCE.commentToPostCommentResponseDTO(comment);

        if (newPostCommentDTO.workstationId() != null) {
            newComment.setCreator(userService.getUserSummaryAsWorkstation(newPostCommentDTO.workstationId(), comment.getCreator().getUsername()));
            newComment.setWorkstation(workstationService.getWorkstationSummaryAsWorkstation(newPostCommentDTO.workstationId(), comment.getWorkstation().getUsername()));
        } else {
            newComment.setCreator(userService.getUserSummaryAsUser(comment.getCreator().getUsername()));
        }

        return newComment;
    }

    public List<PostCommentResponseDTO> getCommentsByPostId(Long postId, Long workstationId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post não encontrado"));
        List<PostComment> postComments = postCommentRepository.findAllByPost_IdOrderByCreatedAtDesc(postId);

        List<PostCommentResponseDTO> postCommentResponseDTOS = new ArrayList<>();
        postComments.forEach(postComment -> {
            PostCommentResponseDTO postCommentResponseDTO = PostMapper.INSTANCE.commentToPostCommentResponseDTO(postComment);

            if (postComment.getWorkstation() != null) {
                if (workstationId != null) {
                    postCommentResponseDTO.setCreator(userService.getUserSummaryAsWorkstation(workstationId, post.getCreator().getUsername()));
                    postCommentResponseDTO.setWorkstation(workstationService.getWorkstationSummaryAsWorkstation(workstationId, postComment.getWorkstation().getUsername()));
                } else {
                    postCommentResponseDTO.setCreator(userService.getUserSummaryAsUser(post.getCreator().getUsername()));
                    postCommentResponseDTO.setWorkstation(workstationService.getWorkstationSummaryAsUser(postComment.getWorkstation().getUsername()));
                }
            } else {
                if (workstationId != null)
                    postCommentResponseDTO.setCreator(userService.getUserSummaryAsWorkstation(workstationId, post.getCreator().getUsername()));
                else
                    postCommentResponseDTO.setCreator(userService.getUserSummaryAsUser(post.getCreator().getUsername()));
            }
            postCommentResponseDTOS.add(postCommentResponseDTO);
        });

        return postCommentResponseDTOS;
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post não encontrado"));

        if (post.getWorkstation() != null) {
            if (!workstationService.isLeader(post.getWorkstation().getId()))
                throw new UnauthorizedException("Você não está autorizado a excluir uma publicação como estação de trabalho.");
        } else {
            User authUser = authUtil.getAuthenticatedUser();
            if (!post.getCreator().getId().equals(authUser.getId()))
                throw new UnauthorizedException("Você não está autorizado a excluir esse post");
        }
        postRepository.delete(post);
    }

    public void deletePostComment(Long commentId) {
        PostComment comment = postCommentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comentario não encontrado"));

        if (comment.getWorkstation() != null) {
            if (!workstationService.isLeader(comment.getWorkstation().getId()))
                throw new UnauthorizedException("Você não está autorizado a excluir esse comentário como estação de trabalho.");
        } else {
            User authUser = authUtil.getAuthenticatedUser();
            if (!comment.getCreator().getId().equals(authUser.getId()))
                throw new UnauthorizedException("Você não está autorizado a excluir esse comentário");
        }

        postCommentRepository.delete(comment);
    }

    public Page<TimelineItemDTO> getUserTimeline(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        User user = authUtil.getAuthenticatedUser();

        Set<Long> userIdsForTimeline = user.getFollowingUsers().stream().map(User::getId).collect(Collectors.toSet());
        userIdsForTimeline.add(user.getId());

        Set<Long> workstationIdsForTimeline = user.getFollowingWorkstations().stream().map(Workstation::getId).collect(Collectors.toSet());

        List<TimelineItemProjection> rawResults = postRepository.getWorkstationTimelineNative(userIdsForTimeline, workstationIdsForTimeline, limit, offset);

        // Mapeia para seu DTO final
        List<TimelineItemDTO> items = rawResults.stream().map(r -> {
            TimelineItemDTO timelineItemDTO = new TimelineItemDTO();
            timelineItemDTO.setId(r.getId());
            timelineItemDTO.setContent(r.getContent());
            timelineItemDTO.setCreatedAt(r.getCreatedAt());
            if (r.getNoteId() != null) {
                timelineItemDTO.setNote(NoteMapper.INSTANCE.noteToNoteTimelinePostDTO(noteRepository.findById(r.getNoteId()).orElse(null)));
            }
            User author = userRepository.findById(r.getCreatorId()).orElse(null);
            timelineItemDTO.setCreator(UserMapper.INSTANCE.userToSummaryDTO(author));

            if (r.getWorkstationId() != null) {
                workstationRepository.findById(r.getWorkstationId()).ifPresent(workstation -> timelineItemDTO.setWorkstation(WorkstationMapper.INSTANCE.workstationToSummaryDTO(workstation)));
            }

            timelineItemDTO.setLiked(postInteractionRepository.existsByPostIdAndCreatorIdAndTypeAndWorkstation_IdIsNull(r.getId(), user.getId(), InteractionType.LIKE));
            timelineItemDTO.setReposted(postInteractionRepository.existsByPostIdAndCreatorIdAndTypeAndWorkstation_IdIsNull(r.getId(), user.getId(), InteractionType.RETWEET));

            if (r.getType().equals("LIKE")) timelineItemDTO.setType(InteractionType.LIKE);
            else if (r.getType().equals("RETWEET")) timelineItemDTO.setType(InteractionType.RETWEET);
            else timelineItemDTO.setType(InteractionType.POST);

            List<Object[]> result = postRepository.countLikesAndRetweetsByPostId(r.getId());

            Map<String, Long> countMap = new HashMap<>();
            for (Object[] row : result) {
                countMap.put((String) row[0], ((Number) row[1]).longValue());
            }
            long likes = countMap.getOrDefault("LIKE", 0L);
            long retweets = countMap.getOrDefault("RETWEET", 0L);
            long comments = postRepository.countCommentsByPostId(r.getId()).orElse(0L);

            timelineItemDTO.setLikesCount(likes);
            timelineItemDTO.setRepostsCount(retweets);
            timelineItemDTO.setCommentsCount(comments);

            return timelineItemDTO;
        }).toList();

        // ⚠️ Aqui é necessário também contar o total de elementos se quiser usar Page<T>
        long total = postRepository.countWorkstationTimelineTotal(userIdsForTimeline, workstationIdsForTimeline); // você cria essa query separada

        return new PageImpl<>(items, pageable, total);
    }


    public Page<TimelineItemDTO> getWorkstationTimeline(Pageable pageable, Long workstationId) {
        Workstation station = workstationRepository.findById(workstationId).orElseThrow(() -> new NotFoundException("Estação não encontrada"));

        if (!workstationService.isMember(workstationId)) {
            throw new UnauthorizedException("Você não pode visualizar essa timeline");
        }

        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        Set<Long> userIdsForTimeline = station.getFollowingUsers().stream().map(User::getId).collect(Collectors.toSet());

        Set<Long> workstationIdsForTimeline = station.getFollowingWorkstations().stream().map(Workstation::getId).collect(Collectors.toSet());
        workstationIdsForTimeline.add(station.getId());

        List<TimelineItemProjection> rawResults = postRepository.getWorkstationTimelineNative(userIdsForTimeline, workstationIdsForTimeline, limit, offset);

        // Mapeia para seu DTO final
        List<TimelineItemDTO> items = rawResults.stream().map(r -> {
            TimelineItemDTO timelineItemDTO = new TimelineItemDTO();
            timelineItemDTO.setId(r.getId());
            timelineItemDTO.setContent(r.getContent());
            timelineItemDTO.setCreatedAt(r.getCreatedAt());
            if (r.getNoteId() != null) {
                timelineItemDTO.setNote(NoteMapper.INSTANCE.noteToNoteTimelinePostDTO(noteRepository.findById(r.getNoteId()).orElse(null)));
            }
            User author = userRepository.findById(r.getCreatorId()).orElse(null);
            timelineItemDTO.setCreator(UserMapper.INSTANCE.userToSummaryDTO(author));

            if (r.getWorkstationId() != null) {
                workstationRepository.findById(r.getWorkstationId()).ifPresent(workstation -> timelineItemDTO.setWorkstation(WorkstationMapper.INSTANCE.workstationToSummaryDTO(workstation)));
            }

            timelineItemDTO.setLiked(postInteractionRepository.existsByPostIdAndWorkstationIdAndType(r.getId(), workstationId, InteractionType.LIKE));
            timelineItemDTO.setReposted(postInteractionRepository.existsByPostIdAndWorkstationIdAndType(r.getId(), workstationId, InteractionType.RETWEET));

            if (r.getType().equals("LIKE")) timelineItemDTO.setType(InteractionType.LIKE);
            else if (r.getType().equals("RETWEET")) timelineItemDTO.setType(InteractionType.RETWEET);
            else timelineItemDTO.setType(InteractionType.POST);

            List<Object[]> result = postRepository.countLikesAndRetweetsByPostId(r.getId());

            Map<String, Long> countMap = new HashMap<>();
            for (Object[] row : result) {
                countMap.put((String) row[0], ((Number) row[1]).longValue());
            }
            long likes = countMap.getOrDefault("LIKE", 0L);
            long retweets = countMap.getOrDefault("RETWEET", 0L);
            long comments = postRepository.countCommentsByPostId(r.getId()).orElse(0L);

            timelineItemDTO.setLikesCount(likes);
            timelineItemDTO.setRepostsCount(retweets);
            timelineItemDTO.setCommentsCount(comments);

            return timelineItemDTO;
        }).toList();

        // ⚠️ Aqui é necessário também contar o total de elementos se quiser usar Page<T>
        long total = postRepository.countWorkstationTimelineTotal(userIdsForTimeline, workstationIdsForTimeline); // você cria essa query separada

        return new PageImpl<>(items, pageable, total);
    }

    public void deletePostInteraction(Long postId, DeletePostInteractionDTO deletePostInteractionDTO) {
        PostInteraction postInteraction;
        User user = authUtil.getAuthenticatedUser();
        if (deletePostInteractionDTO.workstationId() != null) {
            postInteraction = postInteractionRepository.findByPost_IdAndWorkstation_IdAndType(postId, deletePostInteractionDTO.workstationId(), deletePostInteractionDTO.type());
            if (postInteraction != null && !workstationService.isLeader(postInteraction.getWorkstation().getId()))
                throw new UnauthorizedException("Você não pode deletar essa interação");
        } else {
            postInteraction = postInteractionRepository.findByPost_IdAndCreator_IdAndTypeAndWorkstation_IdIsNull(postId, user.getId(), deletePostInteractionDTO.type());
        }

        System.out.println(postInteraction);

        if (postInteraction != null) {
            postInteractionRepository.delete(postInteraction);
        }
    }

    @Transactional
    public TimelineItemDTO getPost(Long postId, Long workstationId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post não encontrado"));

        TimelineItemDTO timelineItemDTO = PostMapper.INSTANCE.postToTimelineItemDTO(post);

        List<Object[]> result = postRepository.countLikesAndRetweetsByPostId(postId);

        Map<String, Long> countMap = new HashMap<>();
        for (Object[] row : result) {
            countMap.put((String) row[0], ((Number) row[1]).longValue());
        }
        long likes = countMap.getOrDefault("LIKE", 0L);
        long retweets = countMap.getOrDefault("RETWEET", 0L);
        long comments = postRepository.countCommentsByPostId(postId).orElse(0L);

        timelineItemDTO.setLikesCount(likes);
        timelineItemDTO.setRepostsCount(retweets);
        timelineItemDTO.setCommentsCount(comments);

        if (workstationId != null) {
            timelineItemDTO.setLiked(postInteractionRepository.existsByPostIdAndWorkstationIdAndType(postId, workstationId, InteractionType.LIKE));
            timelineItemDTO.setReposted(postInteractionRepository.existsByPostIdAndWorkstationIdAndType(postId, workstationId, InteractionType.RETWEET));
        } else {
            User user = authUtil.getAuthenticatedUser();
            timelineItemDTO.setLiked(postInteractionRepository.existsByPostIdAndCreatorIdAndTypeAndWorkstation_IdIsNull(postId, user.getId(), InteractionType.LIKE));
            timelineItemDTO.setReposted(postInteractionRepository.existsByPostIdAndCreatorIdAndTypeAndWorkstation_IdIsNull(postId, user.getId(), InteractionType.RETWEET));
        }

        return timelineItemDTO;
    }
}
