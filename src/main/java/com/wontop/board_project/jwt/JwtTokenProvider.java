package com.wontop.board_project.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;

    // application.yml에서 secret 값 가져와서 key에 저장
    // 디코딩 후 Keys.hmacShaKeyFor()를 통해 HMAC-SHA 알고리즘에 적합한 키 객체를 생성
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public String generateToken(String username, List<String> roles) {
        String authorities = String.join(",", roles); // 리스트를 쉼표로 구분된 문자열로 변환

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 86400000);

        String accessToken = Jwts.builder()
            .setSubject(username)   //사용자 ID
            .claim("auth", authorities) //권한 정보 추가
            .setExpiration(accessTokenExpiresIn)    //만료 시간: 현재 시간 + 1일
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return accessToken;
    }

    // Jwt Access 토큰을 복호화하여 토큰에 들어있는 사용자 인증 정보 반환
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화. 토큰의 페이로드에서 데이터 추출
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) { //auth 필드가 없는 경우
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        // 쉼표로 구분된 권한 문자열을 GrantedAuthority 객체로 변환
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // JWT 토큰이 유효한지, 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key) //토큰의 서명 및 구조 확인
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e); //서명 오류
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e); //토큰 만료
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e); //지원되지 않는 토큰
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e); //클레임이 비어있는 경우
        }
        return false;
    }

    // accessToken에서 claims(토큰 정보) 추출
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
