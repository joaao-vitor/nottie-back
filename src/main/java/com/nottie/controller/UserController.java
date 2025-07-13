package com.nottie.controller;

import com.nottie.dto.request.user.ChangePasswordDTO;
import com.nottie.dto.request.user.EditUserDTO;
import com.nottie.dto.response.user.AuthenticatedUserDTO;
import com.nottie.dto.response.user.SearchUserDTO;
import com.nottie.dto.response.user.SummaryDTO;
import com.nottie.dto.response.user.EditedUserDTO;
import com.nottie.dto.response.workstation.ProfileImgDTO;
import com.nottie.service.UserService;
import com.nottie.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        AuthenticatedUserDTO loggedDTO = userService.getCurrentUser();
        return ResponseUtil.buildSuccessResponse(loggedDTO, "User fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/me/workstations")
    public ResponseEntity<?> getAuthenticatedUserWorkstations() {
        List<?> workstations = userService.getAuthenticatedUserWorkstations();

        return ResponseUtil.buildSuccessResponse(workstations, "User's workstations fetched successfully", HttpStatus.OK);
    }

    @PatchMapping("/me/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO);
        return ResponseUtil.buildSuccessResponse("Password changed successfully", HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody @Valid EditUserDTO editUserDTO) {
        EditedUserDTO editedUserDTO = userService.updateUser(editUserDTO);
        return ResponseUtil.buildSuccessResponse(editedUserDTO, "User updated successfully", HttpStatus.OK);
    }

    @PatchMapping("/me/profile-img")
    public ResponseEntity<?> editUserProfileImg( @RequestParam(value = "image") MultipartFile image) {
        ProfileImgDTO profileImgDTO = userService.editUserProfileImg(image);
        return ResponseUtil.buildSuccessResponse(profileImgDTO, "Profile image updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser() {
        userService.deleteUser();
        return ResponseUtil.buildSuccessResponse("User deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/follow/user/{id}")
    public ResponseEntity<?> followUser(@PathVariable Long id) {
        userService.follow(id, UserService.FollowType.USER);
        return ResponseUtil.buildSuccessResponse("User followed successfully", HttpStatus.OK);
    }


    @PostMapping("/follow/workstation/{id}")
    public ResponseEntity<?> followWorkstation(@PathVariable Long id) {
        userService.follow(id, UserService.FollowType.WORKSTATION);
        return ResponseUtil.buildSuccessResponse("Workstation followed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/unfollow/user/{id}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long id) {
        userService.follow(id, UserService.FollowType.USER);
        return ResponseUtil.buildSuccessResponse("User unfollowed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/unfollow/workstation/{id}")
    public ResponseEntity<?> unfollowWorkstation(@PathVariable Long id) {
        userService.unfollow(id, UserService.FollowType.WORKSTATION);
        return ResponseUtil.buildSuccessResponse("Workstation unfollowed successfully", HttpStatus.OK);
    }

    @GetMapping("/summary/{username}")
    public ResponseEntity<?> getUserSummaryAsUser(@PathVariable String username) {
        SummaryDTO user = userService.getUserSummaryAsUser(username);
        return ResponseUtil.buildSuccessResponse(user, "User summary fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/summary/{workstationId}/{username}")
    public ResponseEntity<?> getUserSummaryAsWorkstation(@PathVariable Long workstationId, @PathVariable String username) {
        SummaryDTO user = userService.getUserSummaryAsWorkstation(workstationId, username);
        return ResponseUtil.buildSuccessResponse(user, "User summary fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<?> searchUser(@PathVariable String username) {
        List<SearchUserDTO> searchUserDTOS = userService.searchUser(username);
        return ResponseUtil.buildSuccessResponse(searchUserDTOS, "User searched successfully", HttpStatus.OK);
    }
}
