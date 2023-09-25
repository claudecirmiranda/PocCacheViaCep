package com.example.demo.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class CacheApiStatus {

    @GetMapping
    public HashMap<String, String>  getStatus() {
        var data = new HashMap<String, String>();
        data.put("Status", "Up!");
        return data;
    }
    
}
