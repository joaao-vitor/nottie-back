package com.nottie.dto.request.post;

import com.nottie.model.InteractionType;

public record DeletePostInteractionDTO(InteractionType type, Long workstationId) {
}
