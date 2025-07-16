package com.nottie.dto.response.note;

public record EditedNoteDTO(
        Long id,
        String title,
        String content
) {
}
