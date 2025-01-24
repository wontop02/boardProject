package com.wontop.board_project.controller;

import com.wontop.board_project.dto.UserDto;
import com.wontop.board_project.dto.UserRequest;
import com.wontop.board_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
//http://localhost:8080/api/user
public class UserApiController {

    private final UserService userService;

    @PostMapping("/join")
    public UserDto join(
        @RequestBody
        UserRequest userRequest
    ){
        return userService.createUser(userRequest);
    }

    @PostMapping("/login")
    public UserDto login(
        @RequestBody
        UserRequest userRequest
    ){
        return userService.login(userRequest);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
