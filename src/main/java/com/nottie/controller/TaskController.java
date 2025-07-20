package com.nottie.controller;

import com.nottie.dto.request.note.EditSingleCategoryValueDTO;
import com.nottie.dto.request.note.NewCategoryValueDTO;
import com.nottie.dto.request.task.NewTaskDTO;
import com.nottie.dto.request.task.TaskCategoryValueDTO;
import com.nottie.dto.response.task.TaskDTO;
import com.nottie.service.TaskService;
import com.nottie.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("@taskService.verifyAccess(#taskId)")
    public ResponseEntity<?> getSingleTask(@PathVariable Long taskId) {
        TaskDTO taskDTO = taskService.getSingleTask(taskId);
        return ResponseUtil.buildSuccessResponse(taskDTO, "Task fetched successfully", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createTask(@RequestBody NewTaskDTO newTaskDTO) {
        TaskDTO taskDTO = taskService.createTask(newTaskDTO);
        return ResponseUtil.buildSuccessResponse(taskDTO, "Task created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}/category/{categoryValueId}")
    public ResponseEntity<?> editSingleCategoryValue(@PathVariable Long taskId, @PathVariable Long categoryValueId, @RequestBody EditSingleCategoryValueDTO editSingleCategoryValueDTO) {
        taskService.editSingleCategory(taskId, categoryValueId, editSingleCategoryValueDTO);
        return ResponseUtil.buildSuccessResponse("Task category value updated successfully", HttpStatus.OK);
    }

    @PostMapping("/{taskId}/category/{categoryId}")
    public ResponseEntity<?> addCategoryValue(@PathVariable Long taskId, @PathVariable Long categoryId, @RequestBody NewCategoryValueDTO taskCatValueDTO) {
        TaskCategoryValueDTO taskCategoryValueDTO = taskService.addCategoryValue(taskId, categoryId, taskCatValueDTO);
        return ResponseUtil.buildSuccessResponse(taskCategoryValueDTO, "Tag inserted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}/category/{categoryId}/{valueId}")
    public ResponseEntity<?> deleteCategoryValue(@PathVariable Long taskId, @PathVariable Long categoryId, @PathVariable Long valueId) {
        taskService.deleteCategoryValue(taskId, categoryId, valueId);
        return ResponseUtil.buildSuccessResponse("Category deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{taskId}/category/{categoryId}/{valueId}")
    public ResponseEntity<?> editCategoryValue(@PathVariable Long taskId, @PathVariable Long categoryId, @PathVariable Long valueId, @RequestBody NewCategoryValueDTO taskCatValueDTO) {
        TaskCategoryValueDTO taskCategoryValueDTO = taskService.editCategoryValue(taskId, categoryId, taskCatValueDTO, valueId);
        return ResponseUtil.buildSuccessResponse(taskCategoryValueDTO, "Tag inserted successfully", HttpStatus.OK);
    }
}
