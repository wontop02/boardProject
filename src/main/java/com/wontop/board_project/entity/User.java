package com.wontop.board_project.entity;

import com.wontop.board_project.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_data") //user는 h2의 예약어이기 때문에 다른 이름으로 변경
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    public boolean checkPasswordRight(String password){
        return this.password.equals(password);
    }

    public UserDto toDto(){
        return UserDto.builder()
            .id(this.id)
            .username(this.username)
            //.password(this.password) UserDto 반환할 때 password 제외
            .build();
    }

}
