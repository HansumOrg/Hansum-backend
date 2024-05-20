package com.example.hansumproject.controller;

import com.example.hansumproject.entity.RefreshEntity;
import com.example.hansumproject.jwt.JWTUtil;
import com.example.hansumproject.repository.RefreshRepository;
import com.example.hansumproject.service.ReissueService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@ResponseBody
public class ReissueController {

    private final ReissueService reissueService;

    public ReissueController(ReissueService reissueService){
        this.reissueService = reissueService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = request.getHeader("refresh");
        if (refresh == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Refresh token null");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (!reissueService.isRefreshTokenValid(refresh)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Refresh token expired or invalid");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (!reissueService.isRefreshTokenAuthorized(refresh)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid refresh token");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        reissueService.deleteOldRefreshToken(refresh);
        String[] tokens = reissueService.generateNewTokens(refresh);
        response.setHeader("access", tokens[0]);
        response.setHeader("refresh", tokens[1]);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Token reissued successfully");
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
