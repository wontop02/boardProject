package com.wontop.board_project.dto;

import com.wontop.board_project.entity.Comment;
import com.wontop.board_project.entity.Like;
import com.wontop.board_project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;

}
