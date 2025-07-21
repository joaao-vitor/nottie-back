package com.nottie.mapper;

import com.nottie.dto.request.task.TaskCategoryValueDTO;
import com.nottie.dto.response.task.TaskDTO;
import com.nottie.model.Task;
import com.nottie.model.TaskCategoryValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    List<TaskDTO> tasksToTaskDTOS(List<Task> tasks);

    List<TaskCategoryValueDTO> taskCategoryValueListToTaskCategoryValueDTOS(List<TaskCategoryValue> noteCategoryValues);

    @Mapping(target = "categories", ignore = true)
    TaskDTO taskToTaskDTO(Task task);

    TaskCategoryValueDTO taskCategoryValueToTaskCategoryValueDTO(TaskCategoryValue taskCategoryValue);
}
