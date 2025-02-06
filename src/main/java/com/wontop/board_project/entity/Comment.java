package com.wontop.board_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wontop.board_project.dto.CommentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) //현재 클래스가 다수의 입장이고, 참조하는 엔티티가 하나일 경우에 사용
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties("comments") // 'comments' 필드를 무시하여 순환 참조 방지
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime commentedAt;

    private String content;

    public boolean equalsCommentUsername(String username) {
        return this.user.getUsername().equals(username);
    }

    public CommentDto toDto() {
        return CommentDto.builder()
            .id(this.id)
            .postId(this.post.getId())
            .username(this.user.getUsername())
            .commentedAt(this.commentedAt)
            .content(this.content)
            .build();
    }
}
