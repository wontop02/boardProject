package com.wontop.board_project.controller;

import com.wontop.board_project.dto.PostDto;
import com.wontop.board_project.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
//http://localhost:8080/api/like
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("")
    public ResponseEntity<String> likeOrUnlikePost(
        Authentication authentication,
        Long post_id
    ){
        String string = likeService.likeOrUnlikePost(authentication, post_id);
        return ResponseEntity.ok(string);
    }
}
