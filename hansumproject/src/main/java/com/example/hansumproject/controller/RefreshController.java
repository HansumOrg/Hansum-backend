package com.example.hansumproject.controller;

import com.example.hansumproject.service.RefreshService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@ResponseBody
public class RefreshController {

    private final RefreshService refreshService;

    public RefreshController(RefreshService refreshService){
        this.refreshService = refreshService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = request.getHeader("refresh");
        if (refresh == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("errorMessage", "Refresh token null");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (!refreshService.isRefreshTokenValid(refresh)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("errorMessage", "Refresh token expired or invalid");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (!refreshService.isRefreshTokenAuthorized(refresh)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("errorMessage", "Invalid refresh token");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        refreshService.deleteOldRefreshToken(refresh);
        String[] tokens = refreshService.generateNewTokens(refresh);
        response.setHeader("access", tokens[0]);
        response.setHeader("refresh", tokens[1]);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Token reissued successfully");
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
