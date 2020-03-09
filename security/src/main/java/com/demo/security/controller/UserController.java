package com.demo.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Fan at 23:09, on 2020/3/8 .
 */
@RestController
@RequestMapping("/user/api")
public class UserController {

    @GetMapping("hello")
    public String hello(){
        return "user权限可以看到";
    }
}
