
package com.wontop.board_project.dto;

import com.wontop.board_project.entity.Post;
import com.wontop.board_project.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentRequest {

    private String content;
}

