package com.wontop.board_project.dto;

import com.wontop.board_project.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private Long id;

    private String username;

    //private String password; 반환할 때 쓰는 UserDto에는 password 제외

}
