package com.nottie.dto.request.tasksgroup;

import com.nottie.model.CategoryType;

public record CreateTasksGroupCategoryDTO(
        String name,
        CategoryType type) {
}
