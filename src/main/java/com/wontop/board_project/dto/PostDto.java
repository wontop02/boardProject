package com.wontop.board_project.dto;

import com.wontop.board_project.entity.Comment;
import com.wontop.board_project.entity.Like;
import com.wontop.board_project.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Builder
public class PostDto {

    private Long id;

    private String title;

    private String content;

    private UserDto userDto; //userDto 사용

    private List<CommentDto> comments;

    private Long likeCount;

    private LocalDateTime postedAt;
}
