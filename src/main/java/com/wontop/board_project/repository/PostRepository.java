package com.wontop.board_project.repository;

import com.wontop.board_project.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post findPostById(Long id);

    @EntityGraph(attributePaths = {"user", "comments"})
    //이렇게 하면 메서드 실행 시 user, comments를 한 번에 조회함. N+1 문제 해결
    List<Post> findAllByOrderByIdAsc(); //오름차순 순서대로 불러옴
}
