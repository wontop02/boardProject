package com.wontop.board_project.service;

import com.wontop.board_project.dto.UserDto;
import com.wontop.board_project.dto.UserRequest;
import com.wontop.board_project.entity.User;
import com.wontop.board_project.jwt.JwtTokenProvider;
import com.wontop.board_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

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
            .roles(new ArrayList<>(List.of("ROLE_USER"))) //role 넣어줌
            .build();

        //role 종류
        //ROLE_USER,
        //ROLE_ADMIN,
        //ROLE_MANAGER

        userRepository.save(user);

        return user.toDto();
    }
    public String login(UserRequest userRequest){
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        //1. 아이디로 사용자 조회
        User user = userRepository.findByUsername(username);

        if (user == null){
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        //2. 비밀번호 검증
        if (!user.checkPasswordRight(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 사용자 역할(Role) 가져오기
        List<String> roles = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)  // GrantedAuthority에서 실제 권한(문자열)을 추출
            .collect(Collectors.toList());

        //인증이 완료되었으므로 따로 인증 없이 JWT 생성
        return jwtTokenProvider.generateToken(username, roles);  // 로그인 시 JWT 반환
    }

    //사용자 이름으로 사용자 찾기

    public User findByUsername(String username) {
    return userRepository.findByUsername(username);
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    public void deleteUser(Long userId, Authentication authentication) {
        // Spring Security User 객체를 커스텀 User 객체로 변환
        org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        // springUser에서 username을 가져와서 커스텀 User를 조회
        com.wontop.board_project.entity.User authenticatedUser = userRepository.findByUsername(springUser.getUsername());

        if (authenticatedUser == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 인증된 사용자가 자신을 삭제하려는지 확인
        if (!authenticatedUser.equalsId(userId)) {
            throw new AccessDeniedException("자신의 계정만 삭제할 수 있습니다.");
        }

        userRepository.delete(authenticatedUser);
    }

}
