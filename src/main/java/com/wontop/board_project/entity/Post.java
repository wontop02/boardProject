package com.wontop.board_project.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id") //user_id를 foreign key로 설정. 보통 참조된 entity의 pk로 만듦
    //참조하는 Entity(Post)의 필드명(user) + "_" + 참조된 Entity(User)의 기본 키 열의 이름(id)
    //user + "_" + id = "user_id"
    private User user;

    @OneToMany(mappedBy = "post", orphanRemoval = true) //내가 하나일 때, 참조하는 객체가 여러 개라면 사용
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Like> likes;

    private LocalDateTime postedAt;
}
