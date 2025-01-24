package com.wontop.board_project.service;

import com.wontop.board_project.dto.UserDto;
import com.wontop.board_project.dto.UserRequest;
import com.wontop.board_project.entity.User;
import com.wontop.board_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserRequest userRequest){
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();

        // 1. 아이디 길이 검사
        if (username.length() < 2 || username.length() > 8) {
            throw new IllegalArgumentException("아이디는 2~8글자 사이여야 합니다.");
        }

        // 2. 비밀번호 길이 및 형식 검사
        if (password.length() < 8 || password.length() > 12 || !password.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            throw new IllegalArgumentException("비밀번호는 영어+숫자가 결합된 8-12글자 사이여야 합니다.");
        }

        // 3. 아이디 중복 검사
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        //모두 통과했다면
        User user = User.builder()
            .username(userRequest.getUsername())
            .password(userRequest.getPassword())
            .build();

        userRepository.save(user);

        return user.toDto();
    }
    public UserDto login(UserRequest userRequest){
        //1. 아이디로 사용자 조회
        User user = userRepository.findByUsername(userRequest.getUsername());

        if (user == null){
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        //2. 비밀번호 검증
        if (!user.checkPasswordRight(userRequest.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //모두 통과하면
        return user.toDto();
    }

    public void ReadUser(){

    }

/*    //로그인 되어 있는 상태에서만 사용, id와 username은 frontend에서 로그인 된 정보로 받음
    public void updateUserPassword(Long id, String username, String currentPassword, String newPassword){
        User currentUser = userRepository.findUserById(id);
        if(currentUser.checkPasswordRight(currentPassword)){
            User newUser = User.builder()
                .id(id)
                .username(username)
                .password(newPassword)
                .build();
            userRepository.save(newUser);
        }
        else{
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }*/

    //사용자 이름으로 사용자 찾기
/*    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }*/

    public void deleteUser(Long id) {
        User user = userRepository.findUserById(id);
        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
