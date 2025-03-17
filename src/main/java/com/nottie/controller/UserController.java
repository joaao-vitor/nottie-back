package com.nottie.controller;

import com.nottie.dto.request.user.EditUserDTO;
import com.nottie.dto.request.user.UserSummaryDTO;
import com.nottie.dto.response.user.EditedUserDTO;
import com.nottie.dto.response.workstation.ProfileImgDTO;
import com.nottie.service.UserService;
import com.nottie.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid EditUserDTO editUserDTO) {
        EditedUserDTO editedUserDTO = userService.updateUser(id, editUserDTO);
        return ResponseUtil.buildSuccessResponse(editedUserDTO, "User updated successfully", HttpStatus.OK);
    }

    @PatchMapping("/{id}/profile-img")
    public ResponseEntity<?> editWorkstationProfileImg(@PathVariable Long id, @RequestParam(value = "image") MultipartFile image) {
        ProfileImgDTO profileImgDTO = userService.editUserProfileImg(id, image);
        return ResponseUtil.buildSuccessResponse(profileImgDTO, "Profile image updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
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

    @GetMapping("/summary/{id}")
    public ResponseEntity<?> getUserSummary(@PathVariable Long id) {
        UserSummaryDTO user = userService.getUserSummary(id);
        return ResponseUtil.buildSuccessResponse(user, "User summary fetched successfully", HttpStatus.OK);
    }
}
