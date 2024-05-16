package com.example.hansumproject.controller;


import com.example.hansumproject.dto.JoinDTO;
import com.example.hansumproject.dto.UserDto;
import com.example.hansumproject.entity.UserEntity;
import com.example.hansumproject.service.JoinService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService){
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public ResponseEntity<Object> joinProcess(@RequestBody @Valid UserDto userDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", bindingResult.getFieldError().getDefaultMessage());
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
}
