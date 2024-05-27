package com.example.hansumproject.controller;


import com.example.hansumproject.dto.JoinDTO;
import com.example.hansumproject.dto.UserDto;
import com.example.hansumproject.entity.UserEntity;
import com.example.hansumproject.service.JoinService;
import com.example.hansumproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;
    private final UserService userService;

    public JoinController(JoinService joinService, UserService userService) {
        this.joinService = joinService;
        this.userService = userService;
    }

    @PostMapping("/join")
    public ResponseEntity<Object> joinProcess(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        UserEntity createdUser = joinService.joinProcess(userDto);

        if (createdUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Registration successful");
        responseBody.put("userId", createdUser.getUserId());
        responseBody.put("name", createdUser.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    // username(로그인ID) 중복 확인
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);

        Map<String, Object> response = new HashMap<>();
        if (exists) {
            response.put("errorMessage", "Username is already in use.");
            response.put("isUsernameAvailable", 0);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            response.put("message", "Username is available");
            response.put("isUsernameAvailable", 1);
            return ResponseEntity.ok(response);
        }
    }

    // nickname 중복확인
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Object>> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.existsByNickname(nickname);

        Map<String, Object> response = new HashMap<>();
        if (exists) {
            response.put("errorMessage", "Nickname is already in use.");
            response.put("isNicknameAvailable", 0);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            response.put("message", "Nickname is available");
            response.put("isNicknameAvailable", 1);
            return ResponseEntity.ok(response);
        }
    }
}
