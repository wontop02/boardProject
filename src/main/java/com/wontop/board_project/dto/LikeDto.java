package com.wontop.board_project.dto;

import com.wontop.board_project.entity.Post;
import com.wontop.board_project.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeDto {

    private Long id;

    private PostDto postDto;

    private UserDto userDto;
}
