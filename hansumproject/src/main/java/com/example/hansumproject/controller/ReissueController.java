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

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        if (!reissueService.isRefreshTokenValid(refresh)) {
            return new ResponseEntity<>("refresh token expired or invalid", HttpStatus.BAD_REQUEST);
        }

        if (!reissueService.isRefreshTokenAuthorized(refresh)) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        reissueService.deleteOldRefreshToken(refresh);
        String[] tokens = reissueService.generateNewTokens(refresh);
        response.setHeader("access", tokens[0]);
        response.setHeader("refresh", tokens[1]);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
