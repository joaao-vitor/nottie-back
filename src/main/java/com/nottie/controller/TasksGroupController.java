package com.nottie.controller;

import com.nottie.dto.request.notesgroup.EditSingleCategoryDTO;
import com.nottie.dto.request.task.TaskCategoryValueDTO;
import com.nottie.dto.request.tasksgroup.CreateTasksGroupCategoryDTO;
import com.nottie.dto.request.tasksgroup.CreateTasksGroupDTO;
import com.nottie.service.TasksGroupService;
import com.nottie.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/tasksgroup")
public class TasksGroupController {

    private final TasksGroupService tasksGroupService;

    public TasksGroupController(TasksGroupService tasksGroupService) {
        this.tasksGroupService = tasksGroupService;
    }

    @PostMapping()
    public ResponseEntity<?> createTasksGroup(@RequestBody CreateTasksGroupDTO tasksGroupDTO) {
        tasksGroupService.createTasksGroup(tasksGroupDTO);
        return ResponseUtil.buildSuccessResponse("Tasks Group created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/workstation/{workstationId}")
    @PreAuthorize("@workstationService.isMember(#workstationId)")
    public ResponseEntity<?> getAllTasksGroupsByWorkstation(@PathVariable Long workstationId) {
        return ResponseUtil.buildSuccessResponse(tasksGroupService.getAllTasksGroupsByWorkstation(workstationId), "Tasks Group fetched successfully", HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAllTasksGroupsByUser() {
        return ResponseUtil.buildSuccessResponse(tasksGroupService.getAllTasksGroupsByUser(), "Tasks Group fetched successfully", HttpStatus.OK);
    }

    @PreAuthorize("@tasksGroupService.verifyMember(#tasksGroupId)")
    @GetMapping("/{tasksGroupId}")
    public ResponseEntity<?> getTasksGroupById(@PathVariable Long tasksGroupId) {
        return ResponseUtil.buildSuccessResponse(tasksGroupService.getTasksGroupById(tasksGroupId), "Tasks Group fetched successfully", HttpStatus.OK);
    }

    @PreAuthorize("@tasksGroupService.verifyLeader(#tasksGroupId)")
    @PostMapping("/{tasksGroupId}/category")
    public ResponseEntity<?> newTasksGroupCategory(@PathVariable Long tasksGroupId, @RequestBody CreateTasksGroupCategoryDTO tasksGroupCategoryDTO) {
        tasksGroupService.newTasksGroupCategory(tasksGroupId, tasksGroupCategoryDTO);
        return ResponseUtil.buildSuccessResponse("Tasks Group category created successfully", HttpStatus.CREATED);
    }

    @PreAuthorize("@tasksGroupService.verifyLeader(#tasksGroupId)")
    @PutMapping("/{tasksGroupId}/category/{categoryId}")
    public ResponseEntity<?> editSingleCategory(@PathVariable Long tasksGroupId, @PathVariable Long categoryId, @RequestBody EditSingleCategoryDTO editSingleCategoryDTO) {
        tasksGroupService.editSingleCategory(tasksGroupId, categoryId, editSingleCategoryDTO);
        return ResponseUtil.buildSuccessResponse("Notes Groups category updated successfully", HttpStatus.OK);
    }

    @PreAuthorize("@tasksGroupService.verifyMember(#tasksGroupId)")
    @GetMapping("/{tasksGroupId}/category/{categoryId}/tag")
    public ResponseEntity<?> getNotesGroupCategoryTags(@PathVariable Long tasksGroupId, @PathVariable Long categoryId) {
        Set<TaskCategoryValueDTO> categories = tasksGroupService.getTasksGroupCategoryTags(tasksGroupId, categoryId);
        return ResponseUtil.buildSuccessResponse(categories, "Notes Groups tags fetched successfully", HttpStatus.OK);
    }
}
