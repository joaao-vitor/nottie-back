package com.nottie.controller;

import com.nottie.dto.request.workstation.CreateWorkstationDTO;
import com.nottie.dto.request.workstation.EditWorkstationDTO;
import com.nottie.dto.request.workstation.GetLeadersDTO;
import com.nottie.dto.response.user.SummaryDTO;
import com.nottie.dto.response.workstation.CreatedWorkstationDTO;
import com.nottie.dto.response.workstation.EditedWorkstationDTO;
import com.nottie.dto.response.workstation.GetMembersDTO;
import com.nottie.dto.response.workstation.ProfileImgDTO;
import com.nottie.service.WorkstationService;
import com.nottie.util.ResponseUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/workstation")
public class WorkstationController {

    private final WorkstationService workstationService;

    public WorkstationController(WorkstationService workstationService) {
        this.workstationService = workstationService;
    }

    @GetMapping("{workstationId}")
    public ResponseEntity<?> getWorkstationSummary(@PathVariable Long workstationId) {
        SummaryDTO workstationSummaryDTO = workstationService.getWorkstationSummary(workstationId);
        return ResponseUtil.buildSuccessResponse(workstationSummaryDTO, "Workstation fetched successfully", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createWorkstation(@RequestBody CreateWorkstationDTO createWorkstationDTO) {
        CreatedWorkstationDTO createdWorkstationDTO = workstationService.createWorkstation(createWorkstationDTO);
        return ResponseUtil.buildSuccessResponse(createdWorkstationDTO, "Workstation created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/{workstationId}")
    @PreAuthorize("@workstationService.isCreator(#workstationId)")
    public ResponseEntity<?> deleteWorkstation(@PathVariable Long workstationId) {
        workstationService.deleteWorkstation(workstationId);
        return ResponseUtil.buildSuccessResponse("Workstation deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{workstationId}")
    @PreAuthorize("@workstationService.isLeader(#workstationId)")
    public ResponseEntity<?> updateWorkstation(@PathVariable Long workstationId, @RequestBody EditWorkstationDTO editWorkstationDTO) {
        EditedWorkstationDTO editedWorkstationDTO = workstationService.editWorkstation(workstationId, editWorkstationDTO);
        return ResponseUtil.buildSuccessResponse(editedWorkstationDTO, "Workstation updated successfully", HttpStatus.OK);
    }

    @PatchMapping("/{workstationId}/profile-img")
    @PreAuthorize("@workstationService.isLeader(#workstationId)")
    public ResponseEntity<?> editWorkstationProfileImg(@PathVariable Long workstationId, @RequestParam(value = "image", required = true) MultipartFile image) {
        ProfileImgDTO profileImgDTO = workstationService.editWorkstationProfileImg(workstationId, image);
        return ResponseUtil.buildSuccessResponse(profileImgDTO, "Profile image updated successfully", HttpStatus.OK);
    }

    @PostMapping("/follow/user/{workstationId}/{userId}")
    @PreAuthorize("@workstationService.isLeader(#workstationId)")
    public ResponseEntity<?> followUser(@PathVariable Long workstationId, @PathVariable Long userId) {
        workstationService.follow(workstationId, userId, WorkstationService.FollowType.USER);

        return ResponseUtil.buildSuccessResponse("User followed successfully", HttpStatus.OK);
    }

    @PostMapping("/follow/workstation/{workstationId}/{followId}")
    @PreAuthorize("@workstationService.isLeader(#workstationId)")
    public ResponseEntity<?> followWorkstation(@PathVariable Long workstationId, @PathVariable Long followId) {
        workstationService.follow(workstationId, followId, WorkstationService.FollowType.WORKSTATION);

        return ResponseUtil.buildSuccessResponse("Workstation followed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/unfollow/user/{workstationId}/{userId}")
    @PreAuthorize("@workstationService.isLeader(#workstationId)")
    public ResponseEntity<?> unfollowUser(@PathVariable Long workstationId, @PathVariable Long userId) {
        workstationService.unfollow(workstationId, userId, WorkstationService.FollowType.USER);

        return ResponseUtil.buildSuccessResponse("User unfollowed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/unfollow/workstation/{workstationId}/{followId}")
    @PreAuthorize("@workstationService.isLeader(#workstationId)")
    public ResponseEntity<?> unfollowWorkstation(@PathVariable Long workstationId, @PathVariable Long followId) {
        workstationService.unfollow(workstationId, followId, WorkstationService.FollowType.WORKSTATION);

        return ResponseUtil.buildSuccessResponse("Workstation unfollowed successfully", HttpStatus.OK);
    }

    @GetMapping("/{workstationId}/members")
    @PreAuthorize("@workstationService.isMember(#workstationId)")
    public ResponseEntity<?> getMembers(@PathVariable Long workstationId, Pageable pageable) {
        GetMembersDTO getMembersDTO = workstationService.getMembers(workstationId, pageable);
        return ResponseUtil.buildSuccessResponse(getMembersDTO, "List of members fetched successfully!", HttpStatus.OK);
    }

    @GetMapping("/{workstationId}/leaders")
    @PreAuthorize("@workstationService.isMember(#workstationId)")
    public ResponseEntity<?> getLeaders(@PathVariable Long workstationId, Pageable pageable) {
        GetLeadersDTO getLeadersDTO = workstationService.getLeaders(workstationId, pageable);
        return ResponseUtil.buildSuccessResponse(getLeadersDTO, "List of leaders fetched successfully!", HttpStatus.OK);
    }

    @PatchMapping("/{workstationId}/add/leader/{leaderId}")
    @PreAuthorize("@workstationService.isCreator(#workstationId)")
    public ResponseEntity<?> addNewLeader(@PathVariable Long workstationId, @PathVariable Long leaderId) {
        workstationService.addNewLeader(workstationId, leaderId);
        return ResponseUtil.buildSuccessResponse("Leader added successfully", HttpStatus.OK);
    }


    @DeleteMapping("/{workstationId}/remove/leader/{leaderId}")
    @PreAuthorize("@workstationService.isCreator(#workstationId)")
    public ResponseEntity<?> removeLeader(@PathVariable Long workstationId, @PathVariable Long leaderId) {
        workstationService.removeLeader(workstationId, leaderId);

        return ResponseUtil.buildSuccessResponse("Leader removed successfully", HttpStatus.OK);
    }

}
