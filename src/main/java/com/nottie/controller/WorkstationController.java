package com.nottie.controller;

import com.nottie.dto.request.workstation.CreateWorkstationDTO;
import com.nottie.dto.response.workstation.CreatedWorkstationDTO;
import com.nottie.service.WorkstationService;
import com.nottie.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workstation")
public class WorkstationController {

    private final WorkstationService workstationService;

    public WorkstationController(WorkstationService workstationService) {
        this.workstationService = workstationService;
    }

    @PostMapping
    public ResponseEntity<?> createWorkstation(@RequestBody CreateWorkstationDTO createWorkstationDTO) {
        CreatedWorkstationDTO createdWorkstationDTO = workstationService.createWorkstation(createWorkstationDTO);
        return ResponseUtil.buildSuccessResponse(createdWorkstationDTO, "Workstation created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/follow/user/{workstationId}/{userId}")
    @PreAuthorize("@workstationService.isLeader(#workstationId)")
    public ResponseEntity<?> followUser(@PathVariable Long workstationId, @PathVariable Long userId) {
        workstationService.follow(workstationId, userId, WorkstationService.FollowType.USER);

        return ResponseUtil.buildSuccessResponse(null, "User followed successfully", HttpStatus.OK);
    }

    @PostMapping("/follow/workstation/{workstationId}/{followId}")
    @PreAuthorize("@workstationService.isLeader(#workstationId)")
    public ResponseEntity<?> followWorkstation(@PathVariable Long workstationId, @PathVariable Long followId) {
        workstationService.follow(workstationId, followId, WorkstationService.FollowType.WORKSTATION);

        return ResponseUtil.buildSuccessResponse(null, "Workstation followed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/unfollow/user/{workstationId}/{userId}")
    @PreAuthorize("@workstationService.isLeader(#workstationId)")
    public ResponseEntity<?> unfollowUser(@PathVariable Long workstationId, @PathVariable Long userId) {
        workstationService.unfollow(workstationId, userId, WorkstationService.FollowType.USER);

        return ResponseUtil.buildSuccessResponse(null, "User unfollowed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/unfollow/workstation/{workstationId}/{followId}")
    @PreAuthorize("@workstationService.isLeader(#workstationId)")
    public ResponseEntity<?> unfollowWorkstation(@PathVariable Long workstationId, @PathVariable Long followId) {
        workstationService.unfollow(workstationId, followId, WorkstationService.FollowType.WORKSTATION);

        return ResponseUtil.buildSuccessResponse(null, "Workstation unfollowed successfully", HttpStatus.OK);
    }
}
