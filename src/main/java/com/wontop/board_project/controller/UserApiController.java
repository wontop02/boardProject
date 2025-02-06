package com.wontop.board_project.controller;

import com.wontop.board_project.dto.UserDto;
import com.wontop.board_project.dto.UserRequest;
import com.wontop.board_project.entity.User;
import com.wontop.board_project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequiredArgsConstructor
@Slf4j
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
    public ResponseEntity<String> login(
        @RequestBody
        UserRequest userRequest
    ){
        String token = userService.login(userRequest); //로그인 후 jwt 반환
        log.info("token : {}", token);
        return ResponseEntity.ok(token); //클라이언트에 jwt 반환
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId, Authentication authentication) {
        try {
            // UserService에서 사용자 삭제 처리
            userService.deleteUser(userId, authentication);
            return ResponseEntity.ok("사용자가 삭제되었습니다.");
        } catch (AccessDeniedException e) {
            // 권한이 없는 경우 403 Forbidden 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // 사용자가 존재하지 않는 경우 404 Not Found 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    //Authorization : Bearer {token}
}
