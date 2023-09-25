package com.example.demo.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    @Cacheable("hello")
    public String hello() {
        System.out.println("** SEM CACHE **");
        return "Olá mundo!";
    }

    @GetMapping("/cancel")
    @CacheEvict("hello")
    public String cancel() {
        System.out.println("** CACHE LIMPO **");
        return "Cache cancelado!";
    }

}
