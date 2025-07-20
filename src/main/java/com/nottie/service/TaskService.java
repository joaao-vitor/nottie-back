package com.nottie.service;

import com.nottie.dto.request.note.EditSingleCategoryValueDTO;
import com.nottie.dto.request.note.NewCategoryValueDTO;
import com.nottie.dto.request.task.NewTaskDTO;
import com.nottie.dto.request.task.TaskCategoryValueDTO;
import com.nottie.dto.request.tasksgroup.TasksGroupCategoryDTO;
import com.nottie.dto.response.task.TaskDTO;
import com.nottie.exception.BadRequestException;
import com.nottie.exception.NotFoundException;
import com.nottie.exception.UnauthorizedException;
import com.nottie.mapper.TaskMapper;
import com.nottie.mapper.TasksGroupMapper;
import com.nottie.model.*;
import com.nottie.repository.TaskCategoryValueRepository;
import com.nottie.repository.TaskRepository;
import com.nottie.repository.TasksGroupCategoryRepository;
import com.nottie.repository.TasksGroupRepository;
import com.nottie.util.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TasksGroupCategoryRepository tasksGroupCategoryRepository;
    private final TasksGroupRepository tasksGroupRepository;
    private final TaskCategoryValueRepository taskCategoryValueRepository;
    private final WorkstationService workstationService;
    private final AuthUtil authUtil;

    public TaskService(TaskRepository taskRepository, TasksGroupCategoryRepository tasksGroupCategoryRepository, TasksGroupRepository tasksGroupRepository, TaskCategoryValueRepository taskCategoryValueRepository, WorkstationService workstationService, AuthUtil authUtil) {
        this.taskRepository = taskRepository;
        this.tasksGroupCategoryRepository = tasksGroupCategoryRepository;
        this.tasksGroupRepository = tasksGroupRepository;
        this.taskCategoryValueRepository = taskCategoryValueRepository;
        this.workstationService = workstationService;
        this.authUtil = authUtil;
    }

    public TaskDTO getSingleTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));
        TaskDTO taskDTO = TaskMapper.INSTANCE.taskToTaskDTO(task);
        Set<TasksGroupCategory> categoryList = tasksGroupCategoryRepository.findAllByGroup_Id(task.getTasksGroup().getId());
        Set<TasksGroupCategoryDTO> categoryDTOS = TasksGroupMapper.INSTANCE.categoriesToCategoriesDTO(categoryList);
        taskDTO.setCategories(categoryDTOS);
        return taskDTO;
    }

    public TaskDTO createTask(NewTaskDTO newTaskDTO) {
        Task task = new Task();
        task.setName(newTaskDTO.name());
        task.setDescription(newTaskDTO.description());
        if (task.getStartDate() != null) {
            task.setStartDate(newTaskDTO.startDate());
        } else {
            task.setStartDate(new Date());
        }
        if (task.getEndDate() != null) {
            task.setEndDate(newTaskDTO.endDate());
        } else {
            task.setEndDate(new Date());
        }
        task.setStatus(StatusType.NOT_STARTED);

        if (newTaskDTO.tasksGroupId() == null) throw new BadRequestException("TasksGroupId cannot be null");

        task.setTasksGroup(tasksGroupRepository.findById(newTaskDTO.tasksGroupId()).orElseThrow(() -> new BadRequestException("TaskGroup not found")));

        taskRepository.save(task);
        Set<TaskCategoryValue> values = new HashSet<>();
        newTaskDTO.categoriesValues().forEach(value -> {
            TaskCategoryValue taskCategoryValue = new TaskCategoryValue();
            TasksGroupCategory category = tasksGroupCategoryRepository.findById(value.getCategory().id()).orElseThrow(() -> new BadRequestException("Category not found"));
            taskCategoryValue.setCategory(category);
            taskCategoryValue.setCheckboxValue(value.getCheckboxValue());
            taskCategoryValue.setTextValue(value.getTextValue());
            taskCategoryValue.setHexColor(value.getHexColor());
            taskCategoryValue.setTask(task);
            taskCategoryValueRepository.save(taskCategoryValue);
            values.add(taskCategoryValue);
        });

        task.setCategoriesValues(values);

        return TaskMapper.INSTANCE.taskToTaskDTO(task);
    }

    public void editSingleCategory(Long taskId, Long categoryValueId, EditSingleCategoryValueDTO editSingleCategoryValueDTO) {
        taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));

        TaskCategoryValue taskCategoryValue = taskCategoryValueRepository.findById(categoryValueId).orElseThrow(() -> new NotFoundException("CategoryValue not found"));

        if (!taskCategoryValue.getCategory().getType().equals(editSingleCategoryValueDTO.type()))
            throw new BadRequestException("A categoria não é a mesma");
        if (!taskCategoryValue.getTask().getId().equals(taskId))
            throw new BadRequestException("Essa categoria não é dessa atividade");

        CategoryType type = taskCategoryValue.getCategory().getType();
        if (type.equals(CategoryType.TEXT)) {
            taskCategoryValue.setTextValue(editSingleCategoryValueDTO.textValue());
        } else if (type.equals(CategoryType.CHECKBOX)) {
            taskCategoryValue.setCheckboxValue(editSingleCategoryValueDTO.checkboxValue());
        } else if (type.equals(CategoryType.TAG)) {
            taskCategoryValue.setHexColor(editSingleCategoryValueDTO.hexColor());
            taskCategoryValue.setTextValue(editSingleCategoryValueDTO.textValue());
        }

        taskCategoryValueRepository.save(taskCategoryValue);
    }

    public TaskCategoryValueDTO addCategoryValue(Long taskId, Long categoryId, NewCategoryValueDTO taskCatValueDTO) {
        Task task = taskRepository.findById((taskId)).orElseThrow(() -> new NotFoundException("Task not found"));
        TasksGroupCategory tasksGroupCategory = tasksGroupCategoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));

        if (!task.getTasksGroup().getId().equals(tasksGroupCategory.getGroup().getId())) {
            throw new BadRequestException("Essa atividade não pertence a essa categoria");
        }

        TaskCategoryValue taskCategoryValue = new TaskCategoryValue();
        taskCategoryValue.setCategory(tasksGroupCategory);
        taskCategoryValue.setTask(task);
        taskCategoryValue.setHexColor(taskCatValueDTO.hexColor());
        taskCategoryValue.setTextValue(taskCatValueDTO.textValue());
        taskCategoryValue.setCheckboxValue(taskCatValueDTO.checkboxValue());
        taskCategoryValueRepository.save(taskCategoryValue);

        return TaskMapper.INSTANCE.taskCategoryValueToTaskCategoryValueDTO(taskCategoryValue);
    }

    public void deleteCategoryValue(Long taskId, Long categoryId, Long valueId) {

        Task task = taskRepository.findById((taskId)).orElseThrow(() -> new NotFoundException("Task not found"));

        TasksGroupCategory tasksGroupCategory = tasksGroupCategoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        TaskCategoryValue taskCategoryValue = taskCategoryValueRepository.findById((valueId)).orElseThrow(() -> new NotFoundException("CategoryValue not found"));

        if (!task.getTasksGroup().getId().equals(tasksGroupCategory.getGroup().getId())) {
            throw new BadRequestException("Grupo de atividade não bate");
        }

        if (!taskCategoryValue.getTask().getId().equals(taskId)) {
            throw new BadRequestException("Essa categoria não é desta atividade");
        }
        taskCategoryValueRepository.deleteById(valueId);
    }


    public TaskCategoryValueDTO editCategoryValue(Long taskId, Long categoryId, NewCategoryValueDTO taskCatValueDTO, Long valueId) {
        Task task = taskRepository.findById((taskId)).orElseThrow(() -> new NotFoundException("Task not found"));

        TasksGroupCategory tasksGroupCategory = tasksGroupCategoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        TaskCategoryValue taskCategoryValue = taskCategoryValueRepository.findById((valueId)).orElseThrow(() -> new NotFoundException("CategoryValue not found"));


        if (!task.getTasksGroup().getId().equals(tasksGroupCategory.getGroup().getId())) {
            throw new BadRequestException("Grupo de atividade não bate");
        }

        if (!taskCategoryValue.getTask().getId().equals(taskId)) {
            throw new BadRequestException("Essa categoria não é desta atividade");
        }

        if (taskCatValueDTO.hexColor() != null) taskCategoryValue.setHexColor(taskCatValueDTO.hexColor());
        if (taskCatValueDTO.textValue() != null) taskCategoryValue.setTextValue(taskCatValueDTO.textValue());
        if (taskCatValueDTO.checkboxValue() != null)
            taskCategoryValue.setCheckboxValue(taskCatValueDTO.checkboxValue());
        taskCategoryValueRepository.save(taskCategoryValue);

        return TaskMapper.INSTANCE.taskCategoryValueToTaskCategoryValueDTO(taskCategoryValue);
    }

    public void verifyAccess(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found "));
        Workstation workstation = task.getTasksGroup().getWorkstation();

        User user = task.getTasksGroup().getCreator();
        if (workstation != null) {
            if (!workstationService.isMember(workstation.getId()))
                throw new UnauthorizedException("Você não é membro dessa estação");
        } else {
            User userAuth = authUtil.getAuthenticatedUser();
            if (!user.getId().equals(userAuth.getId()))
                throw new UnauthorizedException("Você não é o dono desse grupo de atividade");
        }
    }
}
