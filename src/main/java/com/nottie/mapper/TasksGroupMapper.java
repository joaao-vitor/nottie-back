package com.nottie.mapper;

import com.nottie.dto.request.tasksgroup.TasksGroupCategoryDTO;
import com.nottie.dto.response.tasksgroup.GetAllTasksGroupDTO;
import com.nottie.dto.response.tasksgroup.TasksGroupDTO;
import com.nottie.model.TasksGroup;
import com.nottie.model.TasksGroupCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface TasksGroupMapper {
    TasksGroupMapper INSTANCE = Mappers.getMapper(TasksGroupMapper.class);
    List<GetAllTasksGroupDTO> tasksGroupToGetAllTasksGroupDTO(List<TasksGroup> tasksGroups);

    @Mapping(target = "tasks", ignore = true)
    TasksGroupDTO tasksGroupToTasksGroupDTO(TasksGroup tasksGroup);

    Set<TasksGroupCategoryDTO> categoriesToCategoriesDTO(Set<TasksGroupCategory> categoryList);
}
