package com.wontop.board_project.controller;

import com.wontop.board_project.dto.*;
import com.wontop.board_project.entity.Comment;
import com.wontop.board_project.entity.Post;
import com.wontop.board_project.entity.User;
import com.wontop.board_project.service.CommentService;
import com.wontop.board_project.service.LikeService;
import com.wontop.board_project.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
//http://localhost:8080/api/post
public class PostApiController {

    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;

    @GetMapping("")/*
    public String PostList(
        Model model
    ){
        List<PostDto> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "postList";
    }*/
    public ResponseEntity<List<PostDto>> postList() {
        List<PostDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(
        @RequestBody
        PostRequest postRequest,
        Authentication authentication
    ){
        postService.createPost(postRequest, authentication);
        return ResponseEntity.ok("Post created successfully");
    }

    @GetMapping("/{post_id}")
 /*   public String PostDetailView(Long id, Model model){
        //model은 addAttribute를 통해 model에 값 저장. "key", value 조합으로 Hashmap 형태.
        //view file에서 ${key값}을 통해 접근 가능
        model.addAttribute("testboard", postService.postView(id));
        return "postView";
        // return한 string은 view 파일의 이름을 의미.
        // 예를 들어 src/main/resources/templates/boardview.html을 찾아 렌더링함
    }*/
    public ResponseEntity<PostDto> PostDetailView(
        @PathVariable Long post_id,
        Authentication authentication
    ) {
        return ResponseEntity.ok(postService.postView(post_id, authentication));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> delete(
        @PathVariable Long postId,
        Authentication authentication
    ){
        postService.deletePost(postId, authentication);
        return ResponseEntity.ok("Post deleted successfully");
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<String> likeOrUnlikePost(
        Authentication authentication,
        @PathVariable Long postId
    ){
        String string = likeService.likeOrUnlikePost(authentication, postId);
        return ResponseEntity.ok(string);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentList(@PathVariable Long postId) {

        List<CommentDto> comments = commentService.getAllComments(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{postId}/comments/create")
    public CommentDto createComment(
        @PathVariable Long postId,
        @RequestBody
        CommentRequest commentRequest,
        Authentication authentication
    ){
        CommentDto commentDto = commentService.writeComment(postId, commentRequest, authentication);
        return commentDto;
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
        @PathVariable Long commentId,
        Authentication authentication
    ){
        commentService.deleteComment(commentId, authentication);
        return ResponseEntity.ok("Comment deleted successfully");
    }

}
