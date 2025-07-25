package com.nottie.mapper;

import com.nottie.dto.response.post.PostCommentResponseDTO;
import com.nottie.dto.response.timeline.TimelineItemDTO;
import com.nottie.model.Post;
import com.nottie.model.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "workstation", ignore = true)
    PostCommentResponseDTO commentToPostCommentResponseDTO(PostComment postComment);

    TimelineItemDTO postToTimelineItemDTO(Post post);
}
