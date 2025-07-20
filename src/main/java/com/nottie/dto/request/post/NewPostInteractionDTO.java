package com.nottie.dto.request.post;

import com.nottie.model.InteractionType;

public record NewPostInteractionDTO(InteractionType type, Long workstationId) {
}
