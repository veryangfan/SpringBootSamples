package com.demo.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Fan at 23:11, on 2020/3/8 .
 */
@RestController
@RequestMapping("/app/api")
public class AppController {

    @GetMapping("hello")
    public String hello(){
        return "app权限可以看到";
    }
}
