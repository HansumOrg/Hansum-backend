package com.example.hansumproject.jwt;

import com.example.hansumproject.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

public class CustomLogoutFilter extends GenericFilterBean{

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshRepository refreshRepository){
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // "/logout" 으로 요청이 들어왔는지 확인
        //path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        // "POST" 요청인지 확인
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        //get access token
        String access = request.getHeader("access");

        //access Token이 없을 시 401 오류
        //access null check
        if (access == null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.print("{\"errorMessage\": \"Access token is null.\"}");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //access token이 만료되었는지 확인
        //expired check
        try {
            jwtUtil.isExpired(access);
        } catch (ExpiredJwtException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.print("{\"errorMessage\": \"Access token has expired.\"}");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(access);
        if (!category.equals("access")) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.print("{\"errorMessage\": \"Access token has expired.\"}");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //DB에 저장되어 있는지 확인
//        Boolean isExist = refreshRepository.existsByRefresh(access);
//        if (!isExist) {
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            PrintWriter writer = response.getWriter();
//            writer.print("{\"errorMessage\": \"Refresh token not found.\"}");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }

        //로그아웃 진행
        //Access 토큰을 통해 DB에 저장되어 있는 Refresh 토큰 모두 삭제
        Long userId = jwtUtil.getUserId(access);
        refreshRepository.deleteAllByUserId(userId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print("{\"message\": \"Logout successful.\"}");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
