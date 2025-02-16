package com.wontop.board_project.service;

import com.wontop.board_project.entity.Like;
import com.wontop.board_project.entity.Post;
import com.wontop.board_project.entity.User;
import com.wontop.board_project.repository.LikeRepository;
import com.wontop.board_project.repository.PostRepository;
import com.wontop.board_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public String likeOrUnlikePost(Authentication authentication, Long postId) {
        org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        com.wontop.board_project.entity.User user = userRepository.findByUsername(springUser.getUsername());

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Post post = postRepository.findPostById(postId);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }

        boolean alreadyLiked = likeRepository.existsByUserAndPost(user, post);

        // 좋아요 취소
        if (alreadyLiked) {
            likeRepository.deleteByUserAndPost(user, post);
            post.decrementLikeCount();
        }
        // 좋아요 추가
        else {
            Like like = Like.builder()
                .user(user)
                .post(post)
                .build();
            likeRepository.save(like);
            post.incrementLikeCount();
        }

        // 변경 사항 저장
        postRepository.save(post);
        return alreadyLiked ? "좋아요를 취소했습니다." : "좋아요를 눌렀습니다.";
    }
}
