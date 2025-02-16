package com.wontop.board_project.repository;

import com.wontop.board_project.entity.Comment;
import com.wontop.board_project.entity.Like;
import com.wontop.board_project.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentById(Long commentId);

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user WHERE c.post.id = :postId ORDER BY c.id ASC")
    List<Comment> findByPostIdAndOrderByIdAsc(Long postId); //오름차순 순서대로 불러옴

}
