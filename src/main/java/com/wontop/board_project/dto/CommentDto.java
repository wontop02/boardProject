package com.wontop.board_project.dto;

import com.wontop.board_project.entity.Post;
import com.wontop.board_project.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDto {

    private Long id;

    private Long postId;

    private String username;

    private LocalDateTime commentedAt;

    private String content;
}
