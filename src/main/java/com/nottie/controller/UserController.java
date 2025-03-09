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

    @PatchMapping("/follow/{id}")
    public ResponseEntity<?> followUser(@PathVariable Long id) {
        userService.followUser(id);
        return ResponseUtil.buildSuccessResponse(null, "User followed successfully", HttpStatus.OK);
    }

    @PatchMapping("/unfollow/{id}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long id) {
        userService.unfollowUser(id);
        return ResponseUtil.buildSuccessResponse(null, "User unfollowed successfully", HttpStatus.OK);
    }

    @GetMapping("/summary/{id}")
    public ResponseEntity<?> getUserSummary(@PathVariable Long id) {
        UserSummaryDTO user = userService.getUserSummary(id);
        return ResponseUtil.buildSuccessResponse(user, "User summary fetched successfully", HttpStatus.OK);
    }
}
