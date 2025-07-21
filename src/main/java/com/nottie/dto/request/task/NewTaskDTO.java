package com.nottie.dto.request.task;

import com.nottie.model.StatusType;

import java.util.Date;
import java.util.Set;

public record NewTaskDTO(
        String name,
        String description,
        Date startDate,
        Date endDate,
        StatusType status,
        Set<Long> membersId,
        Set<NewTaskCategoryValueDTO> categoriesValues,
        Long tasksGroupId
) {
}
