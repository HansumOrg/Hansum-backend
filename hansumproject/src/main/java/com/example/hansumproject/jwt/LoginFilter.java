package com.example.hansumproject.jwt;

import com.example.hansumproject.dto.CustomUserDetails;
import com.example.hansumproject.dto.UserDto;
import com.example.hansumproject.entity.RefreshEntity;
import com.example.hansumproject.repository.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    private RefreshRepository refreshRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    //AuthenticationFilter 에서 Authentication Manager로 request의 id, password를 넘겨줌
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // 클라이언트 요청에서 JSON 바디를 파싱하여 UserDto 객체로 변환
            UserDto userLogin = new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
            String username = userLogin.getUsername();
            String password = userLogin.getPassword();

            System.out.println("username: " + username);

            // 스프링 시큐리티에서 username과 password를 검증하기 위해 token에 담음
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

            // token에 담은 검증을 위한 AuthenticationManager로 전달
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Request body parsing failed", e);
        }
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        //유저 정보 가지고 옴
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = authentication.getName();
        Long userId = userDetails.getUserId(); // userId 가져오기

        //유저 권한 가지고 옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        //access 토큰 생명주기 10분
        //refresh 토큰 생명 주기 24시간
        String access = jwtUtil.createJwt("access", username, userId, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, userId, role, 86400000L);

        //Refresh token을 DB에 저장
        addRefreshEntity(userId, username, refresh, 86400000L);

        //로그인 성공 시 Header에 access token, refresh token 담아서 보냄
        response.setHeader("access", access);
        response.setHeader("refresh", refresh);

        // 응답 바디에 메시지 추가
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 응답 바디에 JSON 메시지 작성
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"message\": \"Login Success.\"}");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setStatus(HttpStatus.OK.value());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        // 응답 바디에 메시지 추가
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 응답 바디에 JSON 메시지 작성
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"message\": \"Login Fail.\"}");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
    }

    // Refresh token을 DB에 저장하는 메서드
    private void addRefreshEntity(Long userId, String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUserId(userId); // userId refresh
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}
