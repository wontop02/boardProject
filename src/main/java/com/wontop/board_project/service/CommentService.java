package com.wontop.board_project.service;

import com.wontop.board_project.dto.CommentDto;
import com.wontop.board_project.dto.PostDto;
import com.wontop.board_project.dto.CommentRequest;
import com.wontop.board_project.entity.Comment;
import com.wontop.board_project.entity.Post;
import com.wontop.board_project.repository.CommentRepository;
import com.wontop.board_project.repository.PostRepository;
import com.wontop.board_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<CommentDto> getAllComments(Long postId){
        List<Comment> comments = commentRepository.findByPostIdAndOrderByIdAsc(postId);;

        return comments
            .stream()
            .map(Comment::toDto).collect(Collectors.toList());
    }

    public CommentDto writeComment(Long postId, CommentRequest commentRequest, Authentication authentication){
        // Spring Security User 객체를 커스텀 User 객체로 변환
        org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        // springUser에서 username을 가져와서 커스텀 User를 조회
        com.wontop.board_project.entity.User authenticatedUser = userRepository.findByUsername(springUser.getUsername());

        if (authenticatedUser == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }

        String content = commentRequest.getContent();

        //null 검사
        if (content == null){
            throw new IllegalArgumentException("내용을 입력하세요.");
        }

        Comment comment = Comment.builder()
            .content(content)
            .post(post)
            .user(authenticatedUser)
            .commentedAt(LocalDateTime.now())
            .build();

        commentRepository.save(comment);

        return comment.toDto();
    }


    public void deleteComment(Long commentId, Authentication authentication) {

/* 불필요한 과정.. 이라네요. 인증된 사용자가 이미 authentication 객체에 포함되어 있어서
        // Spring Security User 객체를 커스텀 User 객체로 변환
        org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        // springUser에서 username을 가져와서 커스텀 User를 조회
        com.wontop.board_project.entity.User authenticatedUser = userRepository.findByUsername(springUser.getUsername());
*/
        String currentUsername = authentication.getName();
        com.wontop.board_project.entity.User authenticatedUser = userRepository.findByUsername(currentUsername);

        if (authenticatedUser == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        Comment comment = commentRepository.findCommentById(commentId);

        if (comment != null) {
            if(!comment.equalsCommentUsername(authenticatedUser.toDto().getUsername())){
                throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
            }
            commentRepository.delete(comment);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }
}

