package com.example.evcarbon.controller;
//GỌI LOCALHOST 3000 TỚI 8080

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")  // cho phép 3000 gọi 8080
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Backend đang chạy";
    }
}

