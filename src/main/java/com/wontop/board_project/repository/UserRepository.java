package com.wontop.board_project.repository;

import com.wontop.board_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    //Optional은 값이 존재할 수도, 존재하지 않을 수도 있는 상황을 안전하게 처리하기 위해 사용
    //값이 존재하면 Optional은 해당 값 포함, 없으면 비어 있는 empty 상태
    User findByUsername(String username);

    //Optional 없이 id로 찾기
    User findUserById(Long id);

    //아이디 존재 여부 확인
    boolean existsByUsername(String username);

    void deleteByUsername(String username);

}
