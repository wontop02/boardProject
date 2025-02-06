/*
package com.wontop.board_project.service;

import com.wontop.board_project.dto.UserDto;
import com.wontop.board_project.dto.UserRequest;
import com.wontop.board_project.entity.User;
import com.wontop.board_project.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@ActiveProfiles("test") // 테스트 시 "application-test.yml" 설정 사용.
// 특정 프로파일에 맞는 설정을 자동으로 적용함. application-{profile}.yml 등
public class UserServiceTest {

    //@Mock
    @Autowired
    private UserRepository userRepository;


    @Autowired
    //@InjectMocks //Mock된 UserRepository를 UserService에 주ㅜ입
    private UserService userService;

    @Test
    public void testCreateUser_Success(){
        //테스트에 필요한 데이터 정의
        UserRequest joinRequest = new UserRequest("username", "password123");

        //User mockUser = new User(1L, "username", "password123");

        */
/*
        when(mockObject.someMethod()).thenReturn(returnValue)
        when은 Mockito에서 동작을 정의하는 데 사용.
        mock 객체의 특정 메서드가 호출될 때 반환할 값을 설정하기 위해 사용.
        mockObject는 mock 객체, someMethod()가 특정 메서드, thenReturn(returnValue)이 반환 값 지정
        any는 매개변수에 대한 와일드카드. 전달되는 UserEntity의 내용에 관계없이 항상 savedUser 반환
        any를 사용하면 해당 타입의 어떤 객체라도 허용하지만,
        UserEntity.class만 쓰면 특정 클래스의 객체만 의미하기 때문에 save(new UserEntity())와 같이 구체적 객체가 전달되어야만 동작
        *//*


        //when(userRepository.save(any(User.class))).thenReturn(mockUser);

        //실제 서비스 메서드 호출
        UserDto result = userService.createUser(joinRequest);

        //createUser를 했을 때 userRepository.save 메서드가 한 번 호출되었는지 검증
        //메서드가 void일 때는 반환값이 아닌 동작을 검증함. 현재는 Dto반환으로 바꿔서 안 씀
        //verify(userRepository, times(1)).save(any(User.class));

        //toDto 호출 후 검증. 반환 값 없을 때 사용함
        //UserDto result = mockUser.toDto();

        //assertEquals(expected, actual)
        assertEquals(1L, result.getId());
        assertEquals("username", result.getUsername());
        //assertEquals();

    }

    @Test
    public void testJoinValid_InvalidUsername(){

        //너무 짧은 사용자 이름
        UserRequest shortUsername = new UserRequest("a", "password123");
        Exception shortUsernameException = assertThrows(IllegalArgumentException.class, //값이 예상 범위를 벗어나거나 허용되지 않는 형식인 경우 사용
            () -> userService.createUser(shortUsername));
        assertEquals("아이디는 2~8글자 사이여야 합니다.", shortUsernameException.getMessage());
        //<T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable);
        //expectType은 발생을 기대하는 예외 클래스, executable은 예외를 던질 것으로 예상되는 코드 블럭(람다 표현식)


        //너무 긴 사용자 이름
        UserRequest longUsername = new UserRequest("abcdefghij", "password123");
        Exception longUsernameException = assertThrows(IllegalArgumentException.class, //값이 예상 범위를 벗어나거나 허용되지 않는 형식인 경우 사용
            () -> userService.createUser(longUsername));
        assertEquals("아이디는 2~8글자 사이여야 합니다.", longUsernameException.getMessage());
    }

    @Test
    public void testJoinValid_InvalidPassword(){

        //너무 짧은 비밀번호
        UserRequest shortPassword = new UserRequest("username", "pass1");
        Exception shortPasswordException = assertThrows(IllegalArgumentException.class,
            () -> userService.createUser(shortPassword));
        assertEquals("비밀번호는 영어+숫자가 결합된 8-12글자 사이여야 합니다.", shortPasswordException.getMessage());

        //너무 긴 비밀번호
        UserRequest longPassword = new UserRequest("username", "password123456");
        Exception longPasswordException = assertThrows(IllegalArgumentException.class,
            () -> userService.createUser(longPassword));
        assertEquals("비밀번호는 영어+숫자가 결합된 8-12글자 사이여야 합니다.", longPasswordException.getMessage());

        //숫자가 없는 비밀번호
        UserRequest noNumberPassword = new UserRequest("username", "password");
        Exception noNumberPasswordException = assertThrows(IllegalArgumentException.class,
            () -> userService.createUser(noNumberPassword));
        assertEquals("비밀번호는 영어+숫자가 결합된 8-12글자 사이여야 합니다.", noNumberPasswordException.getMessage());

        //영어가 없는 비밀번호
        UserRequest noAlphabetPassword = new UserRequest("username", "12345678");
        Exception noAlphabetPasswordException = assertThrows(IllegalArgumentException.class,
            () -> userService.createUser(noAlphabetPassword));
        assertEquals("비밀번호는 영어+숫자가 결합된 8-12글자 사이여야 합니다.", noAlphabetPasswordException.getMessage());

    }

    @Test
    public void testJoinValid_DuplicateUsername(){
        UserRequest duplicateUsername = new UserRequest("username", "password123");

        //when(userRepository.existsByUsername("username")).thenReturn(true);

        Exception duplicateUsernameException = assertThrows(IllegalArgumentException.class,
            () -> userService.createUser(duplicateUsername));
        assertEquals("이미 사용 중인 아이디입니다.", duplicateUsernameException.getMessage());

        verify(userRepository, times(1)).existsByUsername("username");
    }


    @Test
    public void testUserLogin_Success(){
        UserRequest loginRequest = new UserRequest("username", "password123");

        User mockUser = new User(1L, "username", "password123");
        //when(userRepository.findByUsername("username")).thenReturn(mockUser);

        UserDto result = userService.login(loginRequest);

        assertEquals("username", result.getUsername());
        assertTrue(mockUser.checkPasswordRight("password123"));
    }

    @Test
    public void testUserLogin_UserNotFound() {

        UserRequest loginRequest = new UserRequest("username", "password123");

        //when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.login(loginRequest));

        assertEquals("사용자가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    public void testUserLogin_InvalidPassword(){

        UserRequest loginRequest = new UserRequest("username", "wrongPassword");

        //User mockUser = new User(1L, "username", "password123");

        //when(userRepository.findByUsername("username")).thenReturn(mockUser);

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.login(loginRequest));

        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());

    }

}
*/
