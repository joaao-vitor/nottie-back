package com.nottie.controller;

import com.nottie.dto.request.user.UserSummaryDTO;
import com.nottie.service.UserService;
import com.nottie.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/follow/user/{id}")
    public ResponseEntity<?> followUser(@PathVariable Long id) {
        userService.follow(id, UserService.FollowType.USER);
        return ResponseUtil.buildSuccessResponse(null, "User followed successfully", HttpStatus.OK);
    }

    @PatchMapping("/unfollow/user/{id}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long id) {
        userService.follow(id, UserService.FollowType.USER);
        return ResponseUtil.buildSuccessResponse(null, "User unfollowed successfully", HttpStatus.OK);
    }

    @PatchMapping("/follow/workstation/{id}")
    public ResponseEntity<?> followWorkstation(@PathVariable Long id) {
        userService.follow(id, UserService.FollowType.WORKSTATION);
        return ResponseUtil.buildSuccessResponse(null, "Workstation followed successfully", HttpStatus.OK);
    }

    @PatchMapping("/unfollow/workstation/{id}")
    public ResponseEntity<?> unfollowWorkstation(@PathVariable Long id) {
        userService.unfollow(id, UserService.FollowType.WORKSTATION);
        return ResponseUtil.buildSuccessResponse(null, "Workstation unfollowed successfully", HttpStatus.OK);
    }

    @GetMapping("/summary/{id}")
    public ResponseEntity<?> getUserSummary(@PathVariable Long id) {
        UserSummaryDTO user = userService.getUserSummary(id);
        return ResponseUtil.buildSuccessResponse(user, "User summary fetched successfully", HttpStatus.OK);
    }
}
