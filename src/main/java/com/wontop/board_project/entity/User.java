package com.wontop.board_project.entity;

import com.wontop.board_project.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String username;

    @Getter
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)  // 값 타입 컬렉션임을 나타냄. 권한 목록
    @CollectionTable(
        name = "user_roles",          // 역할을 저장할 테이블 이름
        joinColumns = @JoinColumn(name = "user_id") // 외래 키 설정
    )
    @Column(name = "role")  // 권한 값을 저장할 컬럼 이름
    private List<String> roles = new ArrayList<>();

    public boolean checkPasswordRight(String password){
        return this.password.equals(password);
    }

    //id 동일한지 비교
    public boolean equalsId(Long id){
        return this.id.equals(id);
    }

    public UserDto toDto(){
        return UserDto.builder()
            .id(this.id)
            .username(this.username)
            //.password(this.password) UserDto 반환할 때 password 제외
            .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // roles 리스트를 SimpleGrantedAuthority 객체로 변환하여 반환
        return roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }


/*    @OneToMany(mappedBy = "user", orphanRemoval = true) //mappedBy를 통해 연관 관계의 Owner 명시. owner만이 외래 키를 관리할 수 있고, 아닌 쪽은 읽기 전용
    private List<Post> posts;

    @OneToMany(mappedBy = "user", orphanRemoval = true) //orphanRemoval은 부모와의 연관이 끊어진 고아 엔티티를 자동으로 삭제.
    private List<Comment> comments;                     //부모 엔티티에서 자식 엔티티를 컬렉션에서 제거하면 자식 엔티티가 데이터베이스에서 삭제됨

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Like> likes;*/ //user에는 정보만 넣어라. 활동은 분리. user는 모르고, 다른 객체가 user를 알도록



}
