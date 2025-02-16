package com.wontop.board_project.repository;

import com.wontop.board_project.entity.Like;
import com.wontop.board_project.entity.Post;
import com.wontop.board_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(User user, Post post); //중복 확인. true면 이미 존재
    void deleteByUserAndPost(User user, Post post);
}
