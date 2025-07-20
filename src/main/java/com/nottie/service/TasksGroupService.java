package com.nottie.service;

import com.nottie.dto.request.notesgroup.EditSingleCategoryDTO;
import com.nottie.dto.request.task.TaskCategoryValueDTO;
import com.nottie.dto.request.tasksgroup.CreateTasksGroupCategoryDTO;
import com.nottie.dto.request.tasksgroup.CreateTasksGroupDTO;
import com.nottie.dto.response.task.TaskDTO;
import com.nottie.dto.response.tasksgroup.GetAllTasksGroupDTO;
import com.nottie.dto.response.tasksgroup.TasksGroupDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.exception.UnauthorizedException;
import com.nottie.mapper.*;
import com.nottie.model.*;
import com.nottie.repository.*;
import com.nottie.util.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TasksGroupService {
    private final AuthUtil authUtil;
    private final WorkstationService workstationService;
    private final TasksGroupRepository tasksGroupRepository;
    private final WorkstationRepository workstationRepository;
    private final TaskRepository taskRepository;
    private final TasksGroupCategoryRepository tasksGroupCategoryRepository;
    private final TaskCategoryValueRepository taskCategoryValueRepository;

    public TasksGroupService(AuthUtil authUtil, WorkstationService workstationService, TasksGroupRepository tasksGroupRepository, WorkstationRepository workstationRepository, TaskRepository taskRepository, TasksGroupCategoryRepository tasksGroupCategoryRepository, TaskCategoryValueRepository taskCategoryValueRepository) {
        this.authUtil = authUtil;
        this.workstationService = workstationService;
        this.tasksGroupRepository = tasksGroupRepository;
        this.workstationRepository = workstationRepository;
        this.taskRepository = taskRepository;
        this.tasksGroupCategoryRepository = tasksGroupCategoryRepository;
        this.taskCategoryValueRepository = taskCategoryValueRepository;
    }

    public void createTasksGroup(CreateTasksGroupDTO tasksGroupDTO) {
        TasksGroup tasksGroup = new TasksGroup();
        tasksGroup.setName(tasksGroupDTO.name());

        Workstation workstation;

        if (tasksGroupDTO.workstationId() != null) {
            workstation = workstationRepository.findById(tasksGroupDTO.workstationId()).orElseThrow(() -> new NotFoundException("Workstation not found"));

            if (!workstationService.isLeader(workstation.getId()))
                throw new UnauthorizedException("Você não é lider dessa estação");

            tasksGroup.setWorkstation(workstation);
        }
        tasksGroupRepository.save(tasksGroup);
    }

    public boolean verifyMember(Long tasksGroupId) {
        TasksGroup tasksGroup = tasksGroupRepository.findById(tasksGroupId).orElseThrow(() -> new NotFoundException("TaskGroup not found"));
        User user = authUtil.getAuthenticatedUser();
        if (tasksGroup.getWorkstation() != null)
            return workstationService.isMember(tasksGroup.getWorkstation().getId());
        else
            return tasksGroup.getCreator().getId().equals(user.getId());
    }

    public boolean verifyLeader(Long tasksGroupId) {
        TasksGroup tasksGroup = tasksGroupRepository.findById(tasksGroupId).orElseThrow(() -> new NotFoundException("TaskGroup not found"));
        User user = authUtil.getAuthenticatedUser();
        if (tasksGroup.getWorkstation() != null)
            return workstationService.isLeader(tasksGroup.getWorkstation().getId());
        else
            return tasksGroup.getCreator().getId().equals(user.getId());
    }

    public List<GetAllTasksGroupDTO> getAllTasksGroupsByWorkstation(Long workstationId) {
        workstationRepository.findById(workstationId).orElseThrow(() -> new NotFoundException("Workstation not found"));

        List<TasksGroup> tasksGroups = tasksGroupRepository.findAllByWorkstation_Id(workstationId);
        return TasksGroupMapper.INSTANCE.tasksGroupToGetAllTasksGroupDTO(tasksGroups);
    }

    public List<GetAllTasksGroupDTO> getAllTasksGroupsByUser() {
        User user = authUtil.getAuthenticatedUser();

        List<TasksGroup> tasksGroups = tasksGroupRepository.findAllByCreator_IdAndWorkstation_IdIsNull(user.getId());
        return TasksGroupMapper.INSTANCE.tasksGroupToGetAllTasksGroupDTO(tasksGroups);
    }


    public TasksGroupDTO getTasksGroupById(Long tasksGroupId) {
        TasksGroup tasksGroup = tasksGroupRepository.findById(tasksGroupId).orElseThrow(() -> new NotFoundException("TaskGroup not found"));
        TasksGroupDTO tasksGroupDTO = TasksGroupMapper.INSTANCE.tasksGroupToTasksGroupDTO(tasksGroup);

        List<Task> tasks = taskRepository.findByTasksGroup_Id(tasksGroupId);
        List<TaskDTO> taskDTOS = TaskMapper.INSTANCE.tasksToTaskDTOS(tasks);

        tasksGroupDTO.setTasks(taskDTOS);

        return tasksGroupDTO;
    }

    public void newTasksGroupCategory(Long tasksGroupId, CreateTasksGroupCategoryDTO tasksGroupCategoryDTO) {
        TasksGroup tasksGroup = tasksGroupRepository.findById(tasksGroupId).orElseThrow(() -> new NotFoundException("TaskGroup not found"));

        TasksGroupCategory tasksGroupCategory = new TasksGroupCategory();
        tasksGroupCategory.setName(tasksGroupCategoryDTO.name());
        tasksGroupCategory.setType(tasksGroupCategoryDTO.type());
        tasksGroupCategory.setGroup(tasksGroup);
        tasksGroupCategoryRepository.save(tasksGroupCategory);

        tasksGroup.getCategories().add(tasksGroupCategory);
        tasksGroupRepository.save(tasksGroup);
    }

    public void editSingleCategory(Long tasksGroupId, Long categoryId, EditSingleCategoryDTO editSingleCategoryDTO) {
        TasksGroupCategory tasksGroupCategory = tasksGroupCategoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("TasksGroupCategory not found"));
        if (!tasksGroupCategory.getGroup().getId().equals(tasksGroupId))
            throw new BadRequestException("Essa categoria não pertence a esse grupo");

        if (editSingleCategoryDTO.type() != null) {
            tasksGroupCategory.setType(editSingleCategoryDTO.type());
        }
        if (editSingleCategoryDTO.name() != null) {
            tasksGroupCategory.setName(editSingleCategoryDTO.name());
        }
        tasksGroupCategoryRepository.save(tasksGroupCategory);
    }

    public Set<TaskCategoryValueDTO> getTasksGroupCategoryTags(Long tasksGroupId, Long categoryId) {
        TasksGroupCategory tasksGroupCategory = tasksGroupCategoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("TasksGroupCategory not found"));
        if (!tasksGroupCategory.getGroup().getId().equals(tasksGroupId)) {
            throw new BadRequestException("Categoria não equivale ao grupo de anotações");
        }
        Set<NoteCategoryValue> taskCategoryValues = taskCategoryValueRepository.findAllByCategory_Id(categoryId);
        return TaskMapper.INSTANCE.taskCategoryValueListToTaskCategoryValueDTOS(taskCategoryValues);
    }
}
