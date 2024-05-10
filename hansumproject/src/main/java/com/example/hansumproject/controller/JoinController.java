package com.example.hansumproject.controller;


import com.example.hansumproject.dto.JoinDTO;
import com.example.hansumproject.dto.UserDto;
import com.example.hansumproject.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService){
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public String joinProcess(UserDto userDto){

        joinService.joinProcess(userDto);

        return"ok";
    }
}