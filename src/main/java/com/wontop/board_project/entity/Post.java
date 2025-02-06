package com.wontop.board_project.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wontop.board_project.dto.PostDto;
import com.wontop.board_project.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
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
    @JoinColumn(name = "user_id", nullable = false) //user_id를 foreign key로 설정. 보통 참조된 entity의 pk로 만듦
    //참조하는 Entity(Post)의 필드명(user) + "_" + 참조된 Entity(User)의 기본 키 열의 이름(id)
    //user + "_" + id = "user_id"
    private User user;

    @OneToMany(mappedBy = "post", orphanRemoval = true) //orphanRemoval은 부모가 삭제되면 자식도 삭제
    @Builder.Default //필드 초기화를 보장해 null이 되는 것 방지
    @JsonIgnoreProperties("post") // 'post' 필드를 무시하여 순환 참조 방지
    private List<Comment> comments = new ArrayList<>(); //null 대신 빈 리스트로 초기화
    //List를 사용하면 두 개 이상의 List<> 필드를 JOIN FETCH 할 때 에러 발생
    //Set으로 변경하면 문제 해결
    //그러나 list를 사용해야 순환참조 방지, set은 stream으로 인해 순환 참조가 방지되지 않을 수 있음


    private Long likeCount = 0L;

    private LocalDateTime postedAt;

    //id 동일한지 비교
    public boolean equalsPostUserId(Long id){
        return this.user.equalsId(id);
    }

    public PostDto toDto(){
        return PostDto.builder()
            .id(this.id)
            .title(this.title)
            .content(this.content)
            .userDto(this.user.toDto())  // User 엔티티를 UserDto로 변환
            .postedAt(this.postedAt)
            .comments(this.comments.stream()
                .map(Comment::toDto)  // Comment 엔티티를 CommentDto로 변환
                .collect(Collectors.toList()))
            .likeCount(this.likeCount)
            .build();
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }
}
