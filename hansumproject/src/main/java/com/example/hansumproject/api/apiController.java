package com.example.hansumproject.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@ResponseBody
public class apiController {

    @GetMapping("/")
    public String mainP() {

        return "main Controller";
    }
}
