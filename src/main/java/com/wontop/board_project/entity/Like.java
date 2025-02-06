package com.wontop.board_project.entity;

import com.wontop.board_project.dto.LikeDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`like`", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "post_id"})  // 중복 방지
})
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public LikeDto toDto() {
        return LikeDto.builder()
            .id(this.id)
            .postDto(post.toDto())
            .userDto(user.toDto())
            .build();
    }
}
