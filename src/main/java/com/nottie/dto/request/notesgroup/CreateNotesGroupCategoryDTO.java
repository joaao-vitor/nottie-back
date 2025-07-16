package com.nottie.dto.request.notesgroup;

import com.nottie.model.CategoryType;

public record CreateNotesGroupCategoryDTO(
        String name,
        CategoryType type
) {
}
