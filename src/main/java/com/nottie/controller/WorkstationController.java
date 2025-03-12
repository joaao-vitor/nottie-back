package com.nottie.controller;

import com.nottie.dto.request.workstation.CreateWorkstationDTO;
import com.nottie.dto.response.workstation.CreatedWorkstationDTO;
import com.nottie.service.WorkstationService;
import com.nottie.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
