package com.nottie.dto.request.post;

import jakarta.validation.constraints.Size;

public record NewPostDTO(
        @Size(min = 5, max = 180) String content, Long noteId, Long workstationId) {
}
