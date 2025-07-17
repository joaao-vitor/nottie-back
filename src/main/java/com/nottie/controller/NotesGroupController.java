package com.nottie.controller;

import com.nottie.dto.request.notesgroup.CreateNotesGroupCategoryDTO;
import com.nottie.dto.request.notesgroup.CreateNotesGroupDTO;
import com.nottie.dto.request.notesgroup.EditSingleCategoryDTO;
import com.nottie.dto.response.note.NoteCategoryValueDTO;
import com.nottie.service.NotesGroupService;
import com.nottie.service.WorkstationService;
import com.nottie.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/notesgroup")
public class NotesGroupController {

    private final NotesGroupService notesGroupService;
    private final WorkstationService workstationService;

    public NotesGroupController(NotesGroupService notesGroupService, WorkstationService workstationService) {
        this.notesGroupService = notesGroupService;
        this.workstationService = workstationService;
    }

    @PostMapping()
    public ResponseEntity<?> createNotesGroup(@RequestBody CreateNotesGroupDTO notesGroupDTO) {
        notesGroupService.createNotesGroup(notesGroupDTO);
        return ResponseUtil.buildSuccessResponse("Notes Group created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/workstation/{workstationId}")
    @PreAuthorize("@workstationService.isMember(#workstationId)")
    public ResponseEntity<?> getAllNotesGroupsByWorkstation(@PathVariable Long workstationId) {
        return ResponseUtil.buildSuccessResponse(notesGroupService.getAllNotesGroupByWorkstation(workstationId), "Notes Groups fetched successfully", HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAllNotesGroupsByUser() {
        return ResponseUtil.buildSuccessResponse(notesGroupService.getAllNotesGroupByUser(), "Notes Groups fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/{notesGroupId}")
    public ResponseEntity<?> getNotesGroupById(@PathVariable Long notesGroupId) {
        return ResponseUtil.buildSuccessResponse(notesGroupService.getNotesGroupById(notesGroupId), "Notes Groups fetched successfully", HttpStatus.OK);
    }

    @PostMapping("/{notesGroupId}/category")
    public ResponseEntity<?> newNotesGroupCategory(@PathVariable Long notesGroupId, @RequestBody CreateNotesGroupCategoryDTO notesGroupCategoryDTO) {
        notesGroupService.newNotesGroupCategory(notesGroupId, notesGroupCategoryDTO);
        return ResponseUtil.buildSuccessResponse( "Notes Groups category created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{notesGroupId}/category/{categoryId}")
    public ResponseEntity<?> editSingleCategory(@PathVariable Long notesGroupId, @PathVariable Long categoryId, @RequestBody EditSingleCategoryDTO editSingleCategoryDTO){
        notesGroupService.editSingleCategory(notesGroupId, categoryId, editSingleCategoryDTO);
        return ResponseUtil.buildSuccessResponse("Notes Groups category updated successfully", HttpStatus.OK);
    }

    @GetMapping("/{notesGroupId}/category/{categoryId}/tag")
    public ResponseEntity<?> getNotesGroupCategoryTags(@PathVariable Long notesGroupId, @PathVariable Long categoryId){
        Set<NoteCategoryValueDTO> categories = notesGroupService.getNotesGroupCategoryTags(notesGroupId, categoryId);
        return ResponseUtil.buildSuccessResponse(categories, "Notes Groups tags fetched successfully", HttpStatus.OK);
    }

}
