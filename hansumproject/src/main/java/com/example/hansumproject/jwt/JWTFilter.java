package com.example.hansumproject.jwt;

import com.example.hansumproject.dto.CustomUserDetails;
import com.example.hansumproject.entity.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //client에서 요청 시 token 검증 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 access키에 담긴 토큰을 꺼냄
        //Header에서 access token key를 꺼냄.
        String accessToken = request.getHeader("access");

        // access 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            //access token이 만료되었는지 확인
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //access token이 만료되었다면,
            //response body
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();

            // JSON 형태로 "message" : "access token expired" 출력
            writer.print("{\"errorMessage\": \"access token expired\"}");

            //response status code
            //SC_UNAUTHORIZED 401 코드
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //access token이 만료되면 일단 client에게 특정 코드와 메세지를 남긴 후 종료
            return;
        }

        // 페이로드에 담겨있는 category 값을 확인하여 토큰이 access인지 refresh인지 확인
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();

            // JSON 형태로 "message" : "invalid access token" 출력
            writer.print("{\"errorMessage\": \"invalid access token\"}");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // username, role 값을 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        //UserEntity 생성
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setRole(role);

        //customUserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자(authToken) 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        //다음 필터로 이동
        filterChain.doFilter(request, response);
    }
}
