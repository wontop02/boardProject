package com.wontop.board_project.service;

import com.wontop.board_project.dto.PostDto;
import com.wontop.board_project.dto.PostRequest;
import com.wontop.board_project.entity.Comment;
import com.wontop.board_project.entity.Like;
import com.wontop.board_project.entity.Post;
import com.wontop.board_project.entity.User;
import com.wontop.board_project.repository.PostRepository;
import com.wontop.board_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostDto> getAllPosts(){
        List<Post> posts = postRepository.findAllByOrderByIdAsc();

        return posts
            .stream()
            .map(Post::toDto).collect(Collectors.toList());
    }
    public void createPost(PostRequest postRequest, Authentication authentication){
        String currentUsername = authentication.getName();
        com.wontop.board_project.entity.User authenticatedUser = userRepository.findByUsername(currentUsername);

        if (authenticatedUser == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        String title = postRequest.getTitle();
        String content = postRequest.getContent();

        //null 검사
        if (title == null){
            throw new IllegalArgumentException("제목을 입력하세요.");
        }

        if (content == null){
            throw new IllegalArgumentException("내용을 입력하세요.");
        }

        Post post = Post.builder()
            .title(postRequest.getTitle())
            .content(postRequest.getContent())
            .user(authenticatedUser)
            .likeCount(0L)
            .postedAt(LocalDateTime.now())
            .build();

        postRepository.save(post);
    }

    public void deletePost(Long postId, Authentication authentication) {

        // Spring Security User 객체를 커스텀 User 객체로 변환
        org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        // springUser에서 username을 가져와서 커스텀 User를 조회
        com.wontop.board_project.entity.User authenticatedUser = userRepository.findByUsername(springUser.getUsername());

        if (authenticatedUser == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        Post post = postRepository.findPostById(postId);
        if (post != null) {
            if(!post.equalsPostUserId(authenticatedUser.toDto().getId())){
                throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
            }
            postRepository.delete(post);
        } else {
            throw new RuntimeException("Post not found");
        }
    }

    public PostDto postView(Long post_id, Authentication authentication){
        // Spring Security User 객체를 커스텀 User 객체로 변환
        org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        // springUser에서 username을 가져와서 커스텀 User를 조회
        com.wontop.board_project.entity.User authenticatedUser = userRepository.findByUsername(springUser.getUsername());

        if (authenticatedUser == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        Post post = postRepository.findPostById(post_id); //어떤 게시글을 불러올지 지정
        if (post == null) {
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }

        return post.toDto();
    }

}
